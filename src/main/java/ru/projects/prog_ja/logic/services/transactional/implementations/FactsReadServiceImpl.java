package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.logic.caches.interfaces.FactsCache;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsWriteService;
import ru.projects.prog_ja.model.dao.FactsDAO;

import java.util.List;
import java.util.Random;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FactsReadServiceImpl implements FactsReadService {

    private final FactsDAO factsDAO;
    private final FactsWriteService factsWriteService;
    private final FactsCache factsCache;
    private final ValuesParser parser;

    private final static Random random = new Random();

    private static int commonFactsSize;
    private static int maxEntitiesSize;

    @Autowired
    public FactsReadServiceImpl(FactsDAO factsDAO,
                                FactsCache factsCache,
                                FactsWriteService factsWriteService,
                                ValuesParser parser) {
        this.factsDAO = factsDAO;
        this.factsCache = factsCache;
        this.factsWriteService = factsWriteService;
        this.parser = parser;
    }

    @Override
    public CommonFactTransfer getRandomFact() {

        CommonFactTransfer fact = null;

        int factsCount = factsCache.cacheSize();
        if(factsCount != 0){
            fact = factsCache.getFact(random.nextInt(factsCount));
        }

        if(fact != null){
            return fact;
        }

        long count = factsDAO.getFactsNum();

        return count > 0 ? factsDAO.getFact(random.nextInt((int) count)) : null;
    }

    @Override
    public CommonFactTransfer getNextFact(String fact, String rate, UserDTO userDTO){

        if(fact == null || fact.equals("") || fact.equals("-1") || rate == null || rate.equals("") || rate.equals("0")
                || userDTO == null || userDTO.getId() == -1)
            return getRandomFact();

        else{
            try{
                factsWriteService.updateFactRate(Long.parseLong(fact), Integer.parseInt(rate), userDTO.getId());
            }catch (NumberFormatException e){ }
            return getRandomFact();
        }
    }

    @Override
    public FullFactTransfer getFullFact(long id) {

        return factsDAO.getFullFact(id);
    }

    @Override
    public CommonFactTransfer getFactByTags(List<Long> tags) {

        return getFactByTag(tags.get(random.nextInt(tags.size())));
    }

    @Override
    public CommonFactTransfer getFactByTag(long tagID) {

        CommonFactTransfer fact;

        /*
        * Пытаемся получить факт по тегу из кеша,
        * узнаем длину фактов по этому тегу,
        * наугад выбираем из них и возвращаем
        *
        * Если фактов по данному тегу нет возвращаем обычный факт
        * */
        int countFacts = factsCache.factsByTagSize(tagID);
        if(countFacts != 0){
            fact = factsCache.getFactByTag(tagID, random.nextInt(countFacts));
        }else{
            int countCommonFacts = factsCache.cacheSize();
            fact = factsCache.getFact(random.nextInt(countCommonFacts));
        }

        if(fact != null){
            return fact;
        }

        /*
        * Если кеш не отработал, то возвращаем факты из бд
        * */
        long count = factsDAO.countFactsByTag(tagID);
        if(count == 0){
            return getRandomFact();
        }

        return factsDAO.getFactByTag(tagID, random.nextInt((int) count));
    }

    @Override
    public PageableContainer getFactsByTag(String page, String size, long tagID, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);

        PageableEntity pageable = factsDAO.getFactsByTag((parsedPage - 1) * parsedSize, parsedSize+1,
                tagID,parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getFactsByUser(String page, String size, long userId, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);

        PageableEntity pageable = factsDAO.getFactsByUser((parsedPage-1)*parsedSize, parsedSize+1,
                userId,parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getFacts(String page, String query, String type, String sort) {

        PageableEntity pageable;
        int parsedPage = parser.getPage(page);

        if(query != null && RegexUtil.string(query).matches()){
            pageable = factsDAO.findFacts((parsedPage-1)*commonFactsSize, commonFactsSize, query, getOrderField(type), parser.getSort(sort));
        }else{
            pageable = factsDAO.getFacts((parsedPage-1)*commonFactsSize, commonFactsSize, getOrderField(type), parser.getSort(sort));
        }

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonFactsSize));
    }

    private String getOrderField(String type){

        if(type == null)
            return "rating";

        switch (type){
            case "rating":
                return type;

            case "date":
                return "createDate";

            default:
                return "rating";
        }

    }

    @Value("${facts.common.show.size}")
    public void setCommonFactsSize(int size) {
        commonFactsSize = size;
    }
    @Value("${entities.max.size}")
    public void setMaxEntitiesSize(int size) {
        maxEntitiesSize = size;
    }
}
