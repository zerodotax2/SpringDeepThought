package ru.projects.prog_ja.model.dao;


import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

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

    String getTitle(long articleId);

    PageableEntity getArticles(int start, int size, String orderField, int sort);

    NoticeEntityTemplateDTO getArticleNoticeTemplate(long articleId);

    NoticeCommentTemplateDTO getCommentNoticeTemplate(long commentId);


    PageableEntity getSmallArticles(int start, int size, String orderField, int sort);

    PageableEntity getSmallArticlesByUser(int start, int size, long userId, String query, String orderField, int sort);

    PageableEntity getCommonArticlesByUser(int start, int size, long userId, String query, String orderField, int sort);
    /**
     * @return common articles by search string
     */
    PageableEntity findArticles(int start,  int size, String search, String orderField, int sort);

    /**
     * @return small articles by search string
     */
    PageableEntity findSmallArticles(int start, int size, String search, String orderField, int sort);

    PageableEntity getSmallArticlesByTag(int start, int size, long tagID, String query, String orderField, int sort);

    PageableEntity getCommonArticlesByTag(int start, int size, long tagID, String query, String orderField, int sort);
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

    List<CommonCommentTransfer> getArticleComments(long articleId);
    List<SmallTagTransfer> getSmallArticleTags(long articleId);

    List<Long> getTagsByArticle(long articleId);

    boolean isArticleVoted(long articleId, long userId);

    boolean isArticleCommentVoted(long articleCommentId, long userId);

    long getArticlesNum();
}
