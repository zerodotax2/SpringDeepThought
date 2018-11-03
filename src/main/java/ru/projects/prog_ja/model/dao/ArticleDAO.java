package ru.projects.prog_ja.model.dao;



import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;

import java.util.List;

/**
 *
 * interface that contains methods to work with Post Entity
 */
public interface ArticleDAO {

    /**
    * @return post list start with sort by
     * @param orderField and
     * @param sort
    */

    List<CommonArticleTransfer> getArticles(int start, int size, String orderField, int sort);


    List<SmallArticleTransfer> getSmallArticles(int start, int size, String orderField, int sort);

    List<SmallArticleTransfer> getSmallArticlesByUser(int start, int size, long userId, String orderField, int sort);
    /**
     * @return common articles by search string
     */
    List<CommonArticleTransfer> findArticles(int start,  int size, String search, String orderField, int sort);

    /**
     * @return small articles by search string
     */
    List<SmallArticleTransfer> findSmallArticles(int start, int size, String search, String orderField, int sort);

    List<SmallArticleTransfer> getSmallArticlesByTag(int start, int size, long tagID, String orderField, int sort);

    List<CommonArticleTransfer> getCommonArticlesByTag(int start, int size, long tagID, String orderField, int sort);
    /**
     * @return post by id
     */
    FullArticleTransfer getArticleByID(long id);

    boolean changeRate(long articleId, int rate, long userId);
    /**
     * added post to database
     */
    FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg,
             String title, String subtitle, String htmlContent, List<Long> tags, long userId);

    /**
     * change post with this params
     * */
    boolean updateArticle(long id, String smallImg, String middleImg, String largeImg,
             String title, String subtitle, String htmlContent, List<Long> tags, long userId);

    /**
     * delete post with this id
     */
    boolean deleteArticle(long articleId, long userId);

    /**
     * add comment to post
     * */
    CommonCommentTransfer addComment(long articleId, String comment, long userId);

    boolean removeComment(long commentId, long userId);

    boolean updateComment(long commentId, String comment, long userId);

    boolean changeCommentRate(long commentId, int rate, long userId);

    long getArticlesNum();
}
