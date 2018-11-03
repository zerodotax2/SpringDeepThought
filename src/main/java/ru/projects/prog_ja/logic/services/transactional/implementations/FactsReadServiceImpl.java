package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.caches.interfaces.FactsCache;
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

    private final static Random random = new Random();

    @Value("{facts.common.show.size}")
    private static int commonFactsSize;

    @Value("{entities.max.size}")
    private static int maxEntitiesSize;

    @Autowired
    public FactsReadServiceImpl(FactsDAO factsDAO,
                                FactsCache factsCache,
                                FactsWriteService factsWriteService) {
        this.factsDAO = factsDAO;
        this.factsCache = factsCache;
        this.factsWriteService = factsWriteService;
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

        return factsDAO.getFact(random.nextInt((int) count));
    }

    @Override
    public CommonFactTransfer getNextFact(String fact, String rate, UserDTO userDTO){

        if(fact == null || fact.equals("") || rate == null || rate.equals("")
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
    public BySomethingContainer getFactsByTag(int start, String size, long tagID, String type, String sort) {

        int parsedSize = getSize(size);
        List<CommonFactTransfer> facts = factsDAO.getFactsByTag(start, parsedSize+1,
                tagID, getOrderField(type), getSort(sort));

        return facts != null ? new BySomethingContainer(facts.size() > parsedSize, facts) : null;
    }

    @Override
    public BySomethingContainer getFactsByUser(int start, String size, long userId, String type, String sort) {

        int parsedSize = getSize(size);
        List<CommonFactTransfer> facts = factsDAO.getFactsByUser(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return facts != null ? new BySomethingContainer(facts.size() > parsedSize, facts) : null;
    }

    @Override
    public List<CommonFactTransfer> getFacts(int start, String query, String type, String sort) {
        if(query != null && !query.equals("") && query.matches("^[\\w|\\s]+$")){

            return factsDAO.findFacts(start, commonFactsSize, query, getOrderField(type), getSort(sort));
        }else{

            return factsDAO.getFacts(start, commonFactsSize, getOrderField(type), getSort(sort));
        }
    }


    private int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
    }

    private int getSort(String sort){
        return "0".equals(sort) ? 0 : 1;
    }

    private String getOrderField(String type){

        switch (type){
            case "date":
                return "createDate";

            default:
                return "createDate";
        }

    }
}
