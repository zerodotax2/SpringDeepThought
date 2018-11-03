package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.model.dao.TagsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TagsReadServiceImpl implements TagsReadService {

    private final TagsDAO tagsDAO;
    private final TagsCache tagsCache;

    @Value("{tags.small.show.size}")
    private static int smallTagsSize;

    @Value("{tags.common.show.size}")
    private static int commonTagSize;

    public TagsReadServiceImpl(@Autowired TagsDAO tagsDAO,
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
    public List<CommonTagTransfer> getCommonTags(int start, String type, String sort) {

        return tagsDAO.getCommonTags(start, commonTagSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<CommonTagTransfer> findCommonTags(int start, String search, String type, String sort) {

        if(!search.matches("^[\\w|\\s]+$"))
            return getCommonTags(start, type, sort);

        return tagsDAO.findCommonTags(start, commonTagSize, search, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallTagTransfer> getSmallTags(int start, String type, String sort) {

        return tagsDAO.getSmallTags(start, smallTagsSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallTagTransfer> findSmallTags(int start, String search, String type, String sort) {

        if(!search.matches("^[\\w|\\s]+$"))
            return getSmallTags(start, type, sort);

        return tagsDAO.findTags(start, smallTagsSize, search, getOrderField(type), getSort(sort));
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
    public List<CommonTagTransfer> getTags(int start, String query, String type, String sort){
        if(query != null && !query.equals("")){
            return findCommonTags(start, query, type, sort);
        }else{
            return getCommonTags(start, type, sort);
        }
    }
    
    
    private int getSort(String sort){
        return "0".equals(sort) ? 0 : 1;
    }

    private String getOrderField(String type){

        switch (type){

            case "rating":

                return "rating";
            case "date":

                return "createDate";

            default: return "rating";

        }

    }
}
