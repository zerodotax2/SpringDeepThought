package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.List;

/**
 * interface that contains crud methods with tags
 *
 * @author prog_ja
* */
public interface TagsDAO {

    boolean removeTag(long tagId, long userId);

    /**
    * it is using when we find all by one tag
     * @return small tag and lists:
     * questions, articles, facts
    */
    FullTagTransfer getFullTag(long id);

    String getName(long articleId);

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
    PageableEntity getCommonTags(int start, int size, String orderField, int sort);

    PageableEntity findCommonTags(int start, int size, String search, String orderField, int sort);

    /**
     * create tag with
     * @param name,
     * @param description
     * */
    CommonTagTransfer createTag(String name, String description, String color, long userId);

    /**
     * update tag with this params
     * */
    boolean updateTag( long tagId, String name, String description, String color, long userId);
    /**
     * @return tags by search string
     * */
    PageableEntity findTags(int start, int size, String search, String orderField, int sort);

    /**
    * @return most used tags
    */
    PageableEntity getSmallTags(int start, int size, String orderField, int sort);

    List<SmallTagTransfer> getSmallPopularTags(int start, int size);

    List<CommonTagTransfer> getCommonPopularTags(int start, int size);

    /**
     * return collection of all tags to TagsCache
     * */
    List<CommonTagTransfer> getAllTags();

    List<SmallTagTransfer> getTagsByPrefix(String prefix, int size);

    long getTagsNum();

    PageableEntity getTagsByUser(int start, int size, long userId, String q, String orderField, int sort);
}
