package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsWriteService;
import ru.projects.prog_ja.model.dao.TagsDAO;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class TagsWriteServiceImpl implements TagsWriteService {


    private final TagsDAO tagsDAO;
    private final TagsCache tagsCache;

    @Autowired
    public TagsWriteServiceImpl(TagsDAO tagsDAO, TagsCache tagsCache) {
        this.tagsDAO = tagsDAO;
        this.tagsCache = tagsCache;
    }

    @Override
    public CommonTagTransfer createTag(String name, String description, String color, long userId) {

        CommonTagTransfer tag = tagsDAO.createTag(name, description, color, userId);
        if(tag == null){
            return null;
        }
        tagsCache.putTag(tag);

        return tag;
    }

    @Override
    public boolean updateTag(long tagId, String name, String description, String color, long userId) {

        return tagsDAO.updateTag(tagId,name, description, color, userId);
    }

    @Override
    public boolean removeTag(long tagId, long userId) {

        return tagsDAO.removeTag(tagId, userId);
    }
}
