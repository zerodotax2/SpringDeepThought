package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.logic.services.interfaces.FactsService;
import ru.projects.prog_ja.logic.caches.interfaces.FactsCache;
import ru.projects.prog_ja.model.dao.FactsDAO;

import java.util.List;
import java.util.Random;

@Service
@Scope("prototype")
public class FactsServiceImpl implements FactsService {

    private final FactsDAO factsDAO;
    private final FactsCache factsCache;

    private final static Random random = new Random();

    @Value("{facts.common.show.size}")
    private int commonFactsSize;

    public FactsServiceImpl(@Autowired FactsDAO factsDAO,
                            @Autowired FactsCache factsCache) {
        this.factsDAO = factsDAO;
        this.factsCache = factsCache;
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
    public CommonFactTransfer getFactByTags(List<Long> tags) {

        return factsDAO.getFactByTags(tags);
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
    public CommonFactTransfer createFact(String text, List<Long> tags, long userId) {

        long factId = factsDAO.addFact(text, tags, userId);

        CommonFactTransfer fact = factsDAO.getFact(factId);
        factsCache.putFact(fact);

        return fact;
    }

    @Override
    public List<CommonFactTransfer> getFactsByTag(int start, long tagID, String type, int sort) {

        return factsDAO.getFactsByTag(start, commonFactsSize, tagID, type, sort );
    }

    private String orderField(String type){

        switch (type){
            case "rating":
                return "rating";

            case "date":
                return "createDate";

            default:
                return "rating";
        }

    }
}
