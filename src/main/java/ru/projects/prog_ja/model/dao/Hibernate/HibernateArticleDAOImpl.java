package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.ArticleDAO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.ArticleConverter;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.articles.*;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
public class HibernateArticleDAOImpl extends GenericDAO implements ArticleDAO {


    private final static String  ENTITIES_NAME = "articles";
    private final static String ID_COLUMN = "articleId";

    private final AttachTagService<ArticlesTags> attachTags;
    private final ArticleConverter articleConverter;



    public HibernateArticleDAOImpl(@Autowired SessionFactory sessionFactory,
                                   @Autowired AttachTagService<ArticlesTags> attachTags,
                                   @Autowired ArticleConverter articleConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.articleConverter = articleConverter;
    }

    /**
     * @return Collection of middle articles start from postStart
     * to read it on feed page
     * */
    @Override
    public List<CommonArticleTransfer> getArticles(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonArticleTransfer> query = cb.createQuery(CommonArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);


        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("createDate"),
                article.get("views"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    /**
     * @return Collection of middle articles by
     * @param search string
     * */
    @Override
    public List<CommonArticleTransfer> findArticles(int start,int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonArticleTransfer> query = cb.createQuery(CommonArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("createDate"),
                article.get("views"), article.get("rating")));
        query.where(cb.or(cb.like(article.get("title"), "%"+search.replace(" ", "%")+"%"),
                cb.like(content.get("subtitle"), "%"+search.replace(" ", "%")+"%")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    /**
     * @return Collection of small articles by
     * @param search string
     * */
    @Override
    public List<SmallArticleTransfer> findSmallArticles(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallArticleTransfer> query = cb.createQuery(SmallArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));
        query.where(cb.or(cb.like(article.get("title"), "%"+search.replace(" ", "%")+"%"),
                cb.like(content.get("subtitle"), "%"+search.replace(" ", "%")+"%")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }


    @Override
    public List<SmallArticleTransfer> getSmallArticles(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallArticleTransfer> query = cb.createQuery(SmallArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallArticleTransfer> getSmallArticlesByUser(int start, int size, long userId, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallArticleTransfer> query = cb.createQuery(SmallArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, UserInfo> user = article.join("userInfo");

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));
        query.where(cb.equal(user.get("userId"), userId));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }


    /**
    * @return full post by
     * @param id
    * */
    @Override
    public FullArticleTransfer getArticleByID(long id) {

        ArticleInfo article =  session().createNamedQuery(ArticleInfo.GET_FULL_ARTICLE, ArticleInfo.class)
                .setParameter("id", id).getResultList()
                .stream().findFirst().orElse(null);
        if(article == null){
            return null;
        }


        return articleConverter.fullArticle(article, article.getArticleContent(), article.getArticleComments(),
                article.getTags(), article.getUserInfo());
    }

    @Override
    public boolean changeRate(long articleId, int rate, long userId) {
        try{
            Session session = session();
            return session.createNamedQuery(ArticleInfo.CHANGE_ARTICLE_RATE)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return  false;
        }
    }

    public Set<CommonCommentTransfer> getCommentsByPost(long id){
        Session session = session();

        ArticleInfo articleProxy = session.load(ArticleInfo.class, id);
        if(articleProxy == null){
            return null;
        }

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonCommentTransfer> query = cb.createQuery(CommonCommentTransfer.class);
        Root<ArticleComments> comments = query.from(ArticleComments.class);

        Join<ArticleComments, UserInfo> user = comments.join("userInfo", JoinType.LEFT);

        query.select(cb.construct(CommonCommentTransfer.class,
                comments.get("postCommentId"), comments.get("comment"), comments.get("rating"), comments.get("createDate"),
                user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.equal(comments.get("articleInfo"), articleProxy));

        return new TreeSet<>(session.createQuery(query).getResultList());
    }
    /**
     * create new ArticleInfo(title, tags) and full it with
     * ArticleContent(subtitle, content, images),
     * articleImage(image)
     * */
    @Override
    public FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg,
                              String title, String subtitle, String htmlContent, List<Long> tags, long userId) {

        try{
            Session session = session();

            ArticleInfo articleInfo = new ArticleInfo(title, smallImg, middleImg, largeImg);

            /*Устанавливаем для поста все его ссылочные переменные*/
            Set<ArticlesTags> articlesTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
                articlesTags.add(new ArticlesTags(articleInfo, tag));
            });
            if(articlesTags.size() < 3){
                return null;
            }
            articleInfo.setTags(articlesTags);

            ArticleContent articleContent = new ArticleContent(subtitle, htmlContent);
            articleInfo.setArticleContent(articleContent);

            /*Создаём изображение для поста*/

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return null;

            articleInfo.setUserInfo(user);

            /*Сохраняем пост со всем его контентом*/

            return getArticleByID ((long) session.save(articleInfo));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * update post with this params
     * */
    @Override
    public boolean updateArticle(long id, String smallImg, String middleImg, String largeImg,
                                 String title, String subtitle, String htmlContent, List<Long> tags, long userId) {
        /* ищем по id пост, который хотим изменить, если его нет то делаем return*/
        try {

            Session session = session();

            ArticleInfo articleInfo = session().createNamedQuery(ArticleInfo.GET_FULL_ARTICLE, ArticleInfo.class)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst().orElse(null);
            if (articleInfo == null || articleInfo.getUserInfo().getUserId() != userId)
                return false;

            /*Достаём из поста контент и заполняем его*/
            ArticleContent articleContent = articleInfo.getArticleContent();

            articleContent.setSubtitle(subtitle);
            articleContent.setHtmlContent(htmlContent);

            /*Изменяем изображение поста*/
            articleInfo.setSmallImagePath(smallImg);
            articleInfo.setMiddleImagePath(middleImg);
            articleInfo.setLargeImagePath(largeImg);

            /*Изменяем инфу в самом посте*/
            articleInfo.setTitle(title);

            /*Загружаем все теги*/
            Set<ArticlesTags> articlesTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
                articlesTags.add(new ArticlesTags(articleInfo, tag));
            });
            if (articlesTags.size() < 3) {
                return false;
            }
            articleInfo.setTags(articlesTags);

            session.saveOrUpdate(articleInfo);
        }catch (Exception e){
            return false;
        }

        return true;
    }


    /**
     * delete post with this
     * @param id
     * */
    @Override
    public boolean deleteArticle(long articleId, long userId) {

        try{
            Session session = session();
            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null){
                return false;
            }
            return session.createNamedQuery(ArticleInfo.DELETE_ARTICLE)
                    .setParameter("id", articleId)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * create new comment with comment
     * @param comment and user
     * @param userId
     * to post with id
     * @param articleId
     * */
    @Override
    public CommonCommentTransfer addComment(long articleId, String comment, long userId) {
        try {
            Session session = session();
            ArticleInfo post = session.load(ArticleInfo.class, articleId);
            if(post == null)
                return null;

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return null;

            ArticleComments articleComments = new ArticleComments(comment, user, post);

            return session.createNamedQuery(ArticleInfo.GET_ARTICLE_COMMENT, CommonCommentTransfer.class)
                    .setParameter("id", session.save(articleComments))
                    .getResultList().stream().findFirst().orElse(null);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(ArticleInfo.UPDATE_ARTICLE_COMMENT)
                    .setParameter("comment", comment)
                    .setParameter("id", commentId)
                    .setParameter("user",session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean changeCommentRate(long commentId, int rate, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(ArticleInfo.UPDATE_ARTICLE_COMMENT_RATE)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeComment(long commentId, long userId){
        try {
            Session session = session();
            return session.createNamedQuery(ArticleInfo.DELETE_ARTICLE_COMMENT)
                    .setParameter("id", commentId)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getArticlesNum() {
        return (long) session().createNamedQuery(ArticleInfo.COUNT_ARTICLES, Object.class)
                .getSingleResult();
    }

    @Override
    public List<CommonArticleTransfer> getCommonArticlesByTag(int start, int size, long tagID, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonArticleTransfer> query = cb.createQuery(CommonArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticlesTags> tags = article.join("tags");

        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("createDate"),
                article.get("views"), article.get("rating")));
        query.where(cb.equal(tags.get("tagId"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallArticleTransfer> getSmallArticlesByTag(int start, int size, long tagID, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallArticleTransfer> query = cb.createQuery(SmallArticleTransfer.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticlesTags> tags = article.join("tags");

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));
        query.where(cb.equal(tags.get("tagId"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }
}
