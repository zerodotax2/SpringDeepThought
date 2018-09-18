package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.logic.services.interfaces.TagsService;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.model.dao.TagsDAO;

import java.util.List;

@Service
@Scope("prototype")
public class TagsServiceImpl implements TagsService {

    private final TagsDAO tagsDAO;
    private final TagsCache tagsCache;

    @Value("{tags.small.show.size}")
    private int smallTagsSize;

    @Value("{tags.common.show.size}")
    private int commonTagSize;

    public TagsServiceImpl(@Autowired TagsDAO tagsDAO,
                           @Autowired TagsCache tagsCache) {
        this.tagsDAO = tagsDAO;
        this.tagsCache = tagsCache;
    }

    @Override
    public List<SmallTagTransfer> getPopularTags() {

        List<SmallTagTransfer> popularTags = tagsCache.getPopularTags();
        if(popularTags == null){
            return tagsDAO.getSmallPopularTags(0, smallTagsSize);
        }

        return popularTags;
    }

    @Override
    public List<CommonTagTransfer> getCommonTags(int start, String type, int sort) {

        return tagsDAO.getCommonTags(start, commonTagSize, orderField(type), sort);
    }

    @Override
    public List<CommonTagTransfer> findCommonTags(int start, String search, String type, int sort) {

        return tagsDAO.findCommonTags(start, commonTagSize, search, orderField(type), sort);
    }

    @Override
    public List<SmallTagTransfer> getSmallTags(int start, String type, int sort) {

        return tagsDAO.getSmallTags(start, smallTagsSize, orderField(type), sort);
    }

    @Override
    public List<SmallTagTransfer> findSmallTags(int start, String search, String type, int sort) {

        return tagsDAO.findTags(start, smallTagsSize, search, orderField(type), sort);
    }

    @Override
    public FullTagTransfer getFullTag(long id) {

        return tagsDAO.getFullTag(id);
    }

    @Override
    public CommonTagTransfer getCommonTag(long tagID){

        CommonTagTransfer tag = tagsCache.getCommonTagByID(tagID);
        if(tag == null){
            return tagsDAO.getCommonTag(tagID);
        }

        return tag;
    }

    @Override
    public CommonTagTransfer createTag(String name, String description, String color, long userId) {

        long tagID = tagsDAO.createTag(name, description, color, userId);

        CommonTagTransfer tag = tagsDAO.getCommonTag(tagID);
        tagsCache.putTag(tag);

        return tag;
    }

    private String orderField(String type){

        switch (type){

            case "rating":

                return "rating";
            case "date":

                return "createDate";

            default: return "rating";

        }

    }
}
