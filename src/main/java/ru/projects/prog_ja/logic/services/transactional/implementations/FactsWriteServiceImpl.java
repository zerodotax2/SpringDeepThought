package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.logic.caches.interfaces.FactsCache;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsWriteService;
import ru.projects.prog_ja.model.dao.FactsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class FactsWriteServiceImpl implements FactsWriteService {


    private final FactsDAO factsDAO;
    private final FactsCache factsCache;

    @Autowired
    public FactsWriteServiceImpl(FactsDAO factsDAO, FactsCache factsCache) {
        this.factsDAO = factsDAO;
        this.factsCache = factsCache;
    }

    @Override
    public CommonFactTransfer createFact(String text, List<Long> tags, long userId)  {

        CommonFactTransfer fact = factsDAO.addFact(text, tags, userId);
        factsCache.putFact(fact);

        return fact;
    }

    @Override
    public boolean updateFact(long factId, String text, List<Long> tags, long userId) {

        return factsDAO.updateFact(factId, text, tags, userId);
    }

    @Override
    public boolean updateFactRate(long factId, int rate, long userId) {

        return factsDAO.updateFactRate(factId, getRate(rate), userId);
    }

    @Override
    public boolean deleteFact(long factId, long userId) {

        return factsDAO.deleteFact(factId, userId);
    }


    private void validText(String text) throws BadRequestException{
        if(text.length() < 100 || text.length() > 1000 ||
                !text.matches("^[\\w|\\s]+$")){
            throw new BadRequestException();
        }
    }

    private void validTags(List<Long> tags) throws BadRequestException{
        if(tags.size() < 3 || tags.size() > 5){
            throw new BadRequestException();
        }
    }

    private int getRate(int rate){
        return rate == 1 ? 1 : -1;
    }
}
