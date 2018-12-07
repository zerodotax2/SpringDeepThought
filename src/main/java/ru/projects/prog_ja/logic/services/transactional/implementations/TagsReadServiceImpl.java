package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.model.dao.TagsDAO;

import java.util.Collections;
import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TagsReadServiceImpl implements TagsReadService {

    private final TagsDAO tagsDAO;
    private final TagsCache tagsCache;
    private final ValuesParser parser;

    private static int smallTagsSize;
    private static int commonTagSize;

    @Autowired
    public TagsReadServiceImpl(TagsDAO tagsDAO,
                               TagsCache tagsCache,
                               ValuesParser parser) {
        this.tagsDAO = tagsDAO;
        this.tagsCache = tagsCache;
        this.parser = parser;
    }

    @Override
    public List<SmallTagTransfer> getPopularTags() {

        List<SmallTagTransfer> popularTags = tagsCache.getPopularTags();
        if(popularTags == null){
            popularTags = tagsDAO.getSmallPopularTags(0, smallTagsSize);
        }

        return popularTags == null ? Collections.emptyList() : popularTags;
    }

    @Override
    public String getName(long tagId) {

        String name;
        CommonTagTransfer tag = tagsCache.getCommonTagByID(tagId);
        if(tag != null)
            name = tag.getName();
        else
            name = tagsDAO.getName(tagId);

        return name == null? "" : name;
    }

    @Override
    public PageableContainer getCommonTags(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);

        PageableEntity pageable
                = tagsDAO.getCommonTags((parsedPage-1)*commonTagSize, commonTagSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonTagSize));
    }

    @Override
    public PageableContainer findCommonTags(String page, String search, String type, String sort) {

        if(search == null || !RegexUtil.string(search).matches())
            return getCommonTags(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable
                = tagsDAO.findCommonTags((parsedPage-1)*commonTagSize, commonTagSize, search,
                getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonTagSize));
    }

    @Override
    public PageableContainer getTagsByUser(String page, String size, long userId, String q, String type, String sort) {

        int parsedPage = parser.getPage(page),
            parsedSize = parser.getSize(size);
        PageableEntity pageable = tagsDAO.getTagsByUser((parsedPage-1)*parsedSize,parsedSize, userId, parser.getQuery(q),
                getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getSmallTags(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);
        PageableEntity pageable
                = tagsDAO.getSmallTags((parsedPage-1)*smallTagsSize, smallTagsSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallTagsSize));
    }

    @Override
    public PageableContainer findSmallTags(String page, String search, String type, String sort) {

        if(search == null || !RegexUtil.string(search).matches())
            return getSmallTags(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable
                = tagsDAO.findTags((parsedPage-1)*smallTagsSize, smallTagsSize, search,
                getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallTagsSize));
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
    public PageableContainer getTags(String page, String query, String type, String sort){
        if(query != null && !query.equals("")){
            return findCommonTags(page, query, type, sort);
        }else{
            return getCommonTags(page, type, sort);
        }
    }

    @Override
    public List<SmallTagTransfer> getTagsByPrefix(String prefix) {

        if(prefix == null || !RegexUtil.string(prefix).matches())
            return Collections.emptyList();

        List<SmallTagTransfer> resultList = tagsDAO.getTagsByPrefix(prefix, 5);

        return resultList != null ? resultList : Collections.emptyList();
    }

    private String getOrderField(String type){

        if(type == null)
            return "rating";

        switch (type){

            case "rating":

                return "rating";
            case "date":

                return "createDate";

            default: return "rating";

        }

    }

    @Value("${tags.small.show.size}")
    public  void setSmallTagsSize(int size) {
        smallTagsSize = size;
    }

    @Value("${tags.common.show.size}")
    public  void setCommonTagSize(int size) {
        commonTagSize = size;
    }
}
