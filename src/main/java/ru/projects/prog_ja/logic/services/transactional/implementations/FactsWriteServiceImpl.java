package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.logic.queues.notifications.services.FactNoticeService;
import ru.projects.prog_ja.logic.queues.stats.services.TagCounter;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsWriteService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;
import ru.projects.prog_ja.model.dao.FactsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class FactsWriteServiceImpl implements FactsWriteService {


    private static int RATE_factCreate;

    private final FactsDAO factsDAO;
    private final TagCounter tagCounter;
    private final UserCounter userCounter;
    private final RatingService ratingService;
    private final FactNoticeService factNoticeService;

    @Autowired
    public FactsWriteServiceImpl(FactsDAO factsDAO,
                                 RatingService ratingService,
                                 FactNoticeService factNoticeService,
                                 TagCounter tagCounter,
                                 UserCounter userCounter) {
        this.factsDAO = factsDAO;
        this.ratingService = ratingService;
        this.factNoticeService = factNoticeService;
        this.tagCounter = tagCounter;
        this.userCounter = userCounter;
    }

    @Override
    public CommonFactTransfer createFact(String text, List<Long> tags, long userId)  {

        CommonFactTransfer fact = factsDAO.addFact(text, tags, userId);
        if(fact != null){

            for(long tagID : tags){
                tagCounter.incrementFacts(tagID, 1);
            }
            userCounter.incrementFacts(userId, 1);

            ratingService.updateUserRate(userId, RATE_factCreate);
        }

        return fact;
    }

    @Override
    public boolean updateFact(long factId, String text, List<Long> tags, long userId) {

        List<Long> oldTags = factsDAO.getTagsByFact(factId);
        if(factsDAO.updateFact(factId, text, tags, userId)){

            for (long tagId : oldTags){
                tagCounter.incrementFacts(tagId, -1);
            }
            for(long tagId : tags){
                tagCounter.incrementFacts(tagId, 1);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean updateFactRate(long factId, int rate, long userId) {

        int parsedRate = getRate(rate);
        if(factsDAO.updateFactRate(factId, parsedRate, userId)){



            factNoticeService.factRate(factId, userId);
            ratingService.updateFactOwnerRate(factId, parsedRate);
            return true;
        }

         return false;
    }

    @Override
    public boolean deleteFact(long factId, long userId) {

        if(factsDAO.deleteFact(factId, userId)){
            userCounter.incrementFacts(userId, -1);
            return true;
        }

        return false;
    }


    private int getRate(int rate){
        return rate == 1 ? 1 : -1;
    }

    @Value("${fact.create}")
    public void setRATE_factCreate(int rate) {
        RATE_factCreate = rate;
    }
}
