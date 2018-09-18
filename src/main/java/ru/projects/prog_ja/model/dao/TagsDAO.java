package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;

import java.util.List;

/**
 * interface that contains crud methods with tags
 *
 * @author prog_ja
* */
public interface TagsDAO {

    void delete(long id);

    /**
    * it is using when we find all by one tag
     * @return small tag and lists:
     * questions, articles, facts
    */
    FullTagTransfer getFullTag(long id);

    /**
     * @return tag with name and id
     * */
    SmallTagTransfer getSmallTag(long id);

    /**
     * @return tag with name, id, description,
     * questions size, articles size, facts size
     * */
    CommonTagTransfer getCommonTag(long id);

    /**
     * @return list with CommonTagTransfer dto
     * */
    List<CommonTagTransfer> getCommonTags(int start, int size, String orderField, int sort);

    List<CommonTagTransfer> findCommonTags(int start, int size, String search, String orderField, int sort);

    /**
     * create tag with
     * @param name,
     * @param description
     * */
    long createTag(String name, String description, String color, long userId);

    /**
     * update tag with this params
     * */
    void updateTag(String name, String description, String color, long id);
    /**
     * @return tags by search string
     * */
    List<SmallTagTransfer> findTags(int start, int size, String search, String orderField, int sort);

    /**
    * @return most used tags
    */
    List<SmallTagTransfer> getSmallTags(int start, int size, String orderField, int sort);

    List<SmallTagTransfer> getSmallPopularTags(int start, int size);

    List<CommonTagTransfer> getCommonPopularTags(int start, int size);

    /**
     * return collection of all tags to TagsCache
     * */
    List<CommonTagTransfer> getAllTags();

    long getTagsNum();
}
