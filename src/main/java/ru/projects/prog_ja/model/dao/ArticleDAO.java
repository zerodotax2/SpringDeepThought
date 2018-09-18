package ru.projects.prog_ja.model.dao;



import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;

import java.util.List;

/**
 *
 * interface that contains methods to work with Post Entity
 */
public interface ArticleDAO {

    /**
    * @return post list start with
     * @param postStart
    */

    List<CommonArticleTransfer> getArticles(int start, int size, String type, int sort);


    List<SmallArticleTransfer> getSmallArticles(int start, int size, String type, int sort);

    List<SmallArticleTransfer> getSmallArticlesByUser(int start, int size,long userId, String type, int sort);
    /**
     * @return common articles by search string
     */
    List<CommonArticleTransfer> findArticles(int start,  int size, String search, String type, int sort);

    /**
     * @return small articles by search string
     */
    List<SmallArticleTransfer> findSmallArticles(int start, int size, String search, String type, int sort);

    List<SmallArticleTransfer> getSmallArticlesByTag(int start, int size, long tagID, String orderField, int sort);

    List<CommonArticleTransfer> getCommonArticlesByTag(int start, int size, long tagID, String orderField, int sort);
    /**
     * @return post by id
     */
    FullArticleTransfer getArticleByID(long id);

    /**
     * added post to database
     */
    long createArticle(List<String> mainImages, String title, String subtitle, String htmlContent, List<Long> tags, long userId);

    /**
     * change post with this params
     * */
    boolean updateArticle(long id, List<String> mainImages, String title, String subtitle, String htmlContent, List<Long> tags);

    /**
     * delete post with this id
     */
    boolean deleteArticle(long id);

    /**
     * add comment to post
     * */
    boolean addComment(long articleId, String comment, long userId);

    void updateComment(long commentId, String comment);

    long getArticlesNum();
}
