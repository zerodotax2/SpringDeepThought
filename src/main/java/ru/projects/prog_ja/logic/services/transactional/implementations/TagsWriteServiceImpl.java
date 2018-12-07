package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsWriteService;
import ru.projects.prog_ja.model.dao.TagsDAO;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class TagsWriteServiceImpl implements TagsWriteService {

    private static int RATE_tagCreate;
    private static int RATE_tagUser;

    private final RatingService ratingService;
    private final TagsDAO tagsDAO;
    private final UserCounter userCounter;

    @Autowired
    public TagsWriteServiceImpl(TagsDAO tagsDAO,
                                RatingService ratingService,
                                UserCounter userCounter) {
        this.tagsDAO = tagsDAO;
        this.ratingService = ratingService;
        this.userCounter = userCounter;
    }

    @Override
    public CommonTagTransfer createTag(String name, String description, String color, long userId) {

        CommonTagTransfer tag = tagsDAO.createTag(name, description, color, userId);
        if(tag != null){

            userCounter.incrementTags(userId, 1);
            ratingService.updateUserRate(userId, RATE_tagCreate);
        }

        return tag;
    }

    @Override
    public boolean updateTag(long tagId, String name, String description, String color, long userId) {

        return tagsDAO.updateTag(tagId,name, description, color, userId);
    }

    @Override
    public boolean removeTag(long tagId, long userId) {

        if(tagsDAO.removeTag(tagId, userId)){

            userCounter.incrementTags(userId, -1);
            return true;
        }
        return false;
    }

    @Value("${tag.create}")
    public  void setRATE_tagCreate(int rate) {
        RATE_tagCreate = rate;
    }

    @Value("${tag.use}")
    public  void setRATE_tagUser(int rate) {
        RATE_tagUser = rate;
    }
}
