package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.ViewArticleTransfer;
import ru.projects.prog_ja.model.dao.ArticleDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.ArticleConverter;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.queries.ArticleQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.entity.articles.*;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
public class HibernateArticleDAOImpl extends GenericDAO implements ArticleDAO {


    private final static String ENTITIES_NAME = "articles";
    private final static String ID_COLUMN = "articleId";

    private final AttachTagService<ArticlesTags, ArticleInfo> attachTags;
    private final ArticleConverter articleConverter;


    @Autowired
    public HibernateArticleDAOImpl(SessionFactory sessionFactory,
                                   AttachTagService<ArticlesTags, ArticleInfo> attachTags,
                                   ArticleConverter articleConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.articleConverter = articleConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeEntityTemplateDTO getArticleNoticeTemplate(long articleId) {

        return session().createNamedQuery(ArticleQueries.GET_NOTICE_TEMPLATE, NoticeEntityTemplateDTO.class)
                .setParameter("id", articleId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeCommentTemplateDTO getCommentNoticeTemplate(long commentId) {

        return session().createNamedQuery(ArticleQueries.GET_NOTICE_COMMENT_TEMPLATE, NoticeCommentTemplateDTO.class)
                .setParameter("id", commentId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public String getTitle(long articleId) {
        return session().createNamedQuery(ArticleQueries.GET_TITLE, String.class)
                .setParameter("id", articleId)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * @return Collection of middle articles start from postStart
     * to read it on feed page
     * */
    @Override
    public PageableEntity getArticles(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("middleImagePath"), article.get("createDate"),
                article.get("views"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity( attachTags.tags((List)
                session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    /**
     * @return Collection of middle articles by
     * @param search string
     * */
    @Override
    public PageableEntity findArticles(int start,int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        query.where(cb.or(cb.like(article.get("title"), "%"+search.replace(" ", "%")+"%"),
                cb.like(content.get("subtitle"), "%"+search.replace(" ", "%")+"%")));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count){
            return new PageableEntity(Collections.emptyList(), count);
        }

        query.select(cb.construct(CommonArticleTransfer.class,
                    article.get("articleId"), article.get("title"), article.get("middleImagePath"), article.get("createDate"),
                    article.get("views"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    /**
     * @return Collection of small articles by
     * @param search string
     * */
    @Override
    public PageableEntity findSmallArticles(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        query.where(cb.or(cb.like(article.get("title"), "%"+search.replace(" ", "%")+"%"),
                cb.like(content.get("subtitle"), "%"+search.replace(" ", "%")+"%")));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity(attachTags.tags( (List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }


    @Override
    public PageableEntity getSmallArticles(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("smallImagePath"), article.get("rating")));

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity( attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getSmallArticlesByUser(int start, int size, long userId, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, UserInfo> user = article.join("userInfo", JoinType.LEFT);
        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        if(q != null)
            query.where(cb.and(cb.equal(user.get("userId"), userId),
                    cb.or(cb.like(article.get("title"), "%"+q.replace(" ", "%")+"%"),
                            cb.like(content.get("subtitle"), "%"+q.replace(" ", "%")+"%"))));
        else
            query.where(cb.equal(user.get("userId"), userId));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(ViewArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("middleImagePath")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        List<ViewArticleTransfer> articles =
                (List) session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();

        return new PageableEntity(articles , count);
    }


    /**
    * @return full post by
     * @param id
    * */
    @Override
    public FullArticleTransfer getArticleByID(long id) {

        Session session = session();

        FullArticleTransfer article =  session.createNamedQuery(ArticleQueries.GET_FULL_ARTICLE, FullArticleTransfer.class)
                .setParameter("id", id).getResultList()
                .stream().findFirst().orElse(null);
        if(article == null){
            return null;
        }

        List<SmallTagTransfer> tags = getSmallArticleTags(id);
        if(tags == null)
            article.setTags(Collections.emptySet());
        else
            article.setTags(new HashSet<>(tags));

        List<CommonCommentTransfer> comments = getArticleComments(id);
        if(comments == null)
            article.setComments(Collections.emptySet());
        else
            article.setComments(new TreeSet<>(comments));


        return article;
    }

    @Override
    public List<CommonCommentTransfer> getArticleComments(long articleId) {

        Session session = session();
        ArticleInfo article = session.load(ArticleInfo.class, articleId);

        return session.createNamedQuery(ArticleQueries.GET_ARTICLE_COMMENTS, CommonCommentTransfer.class)
                .setParameter("article", article)
                .getResultList();
    }

    @Override
    public List<SmallTagTransfer> getSmallArticleTags(long articleId) {

        Session session = session();
        ArticleInfo article = session.load(ArticleInfo.class, articleId);

        return session.createNamedQuery(ArticleQueries.GET_SMALL_ARTICLE_TAGS, SmallTagTransfer.class)
                .setParameter("article", article)
                .getResultList();
    }

    @Override
    public boolean isArticleVoted(long articleId, long userId) {

        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        ArticleInfo article = session.load(ArticleInfo.class, articleId);

        return !session.createNamedQuery(ArticleQueries.IS_ARTICLE_VOTED, Long.class)
                .setParameter("article", article)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isArticleCommentVoted(long articleCommentId, long userId) {

        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        ArticleComments comment = session.load(ArticleComments.class, articleCommentId);

        return !session.createNamedQuery(ArticleQueries.IS_COMMENT_ARTICLE_VOTED, Long.class)
                .setParameter("comment", comment)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean changeRate(long articleId, int rate, long userId) {
        try{
            Session session = session();

            ArticleInfo article = session.load(ArticleInfo.class, articleId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new ArticleVoters(article, user));

            return session.createNamedQuery(ArticleQueries.CHANGE_ARTICLE_RATE)
                    .setParameter("id", articleId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return  false;
        }
    }

    public Set<CommonCommentTransfer> getCommentsByPost(long id){
        Session session = session();

        ArticleInfo articleProxy = session.load(ArticleInfo.class, id);

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
            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            if(tagsList == null || tagsList.size() < 2){
                return null;
            }
            Set<ArticlesTags> tagsSet = new HashSet<>(tagsList.size());
            tagsList.forEach(tag -> tagsSet.add(new ArticlesTags(articleInfo, tag)));
            articleInfo.setTags(tagsSet);

            ArticleContent articleContent = new ArticleContent(subtitle, htmlContent);
            articleContent.setArticle(articleInfo);
            articleInfo.setArticleContent(articleContent);

            /*Создаём изображение для поста*/

            UserInfo user = session.load(UserInfo.class, userId);

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

            ArticleInfo articleInfo = session.createNamedQuery(ArticleQueries.GET_UPDATE_ENTITY_ARTICLE, ArticleInfo.class)
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
            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            articleInfo.setTags(updateTags(articleInfo, tagsList));

            session.save(articleInfo);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    private Set<ArticlesTags> updateTags(ArticleInfo article, List<Tags> newTags){

        Set<ArticlesTags> oldTags = article.getTags();

        if(newTags == null || newTags.size() < 2){
            return Collections.emptySet();
        }

        /*
        * Перебираем по новым тегам, если есть совпадение удаляем из старых и новых тегов
        * */
        Tags t;ArticlesTags old;
        for(Iterator<Tags> i = newTags.iterator(); i.hasNext();){

            t = i.next();

            for(Iterator<ArticlesTags> j = oldTags.iterator(); j.hasNext();) {
                old = j.next();
                if (old.getTagId().getTagId() == t.getTagId()) {
                    j.remove();
                    i.remove();
                    break;
                }
            }
        }
        /*
        * Всё что осталось в старых тегах удаляем
        * */
        List<Tags> tagsToDelete = new ArrayList<>(oldTags.size());
        for(ArticlesTags tag: oldTags)
            tagsToDelete.add(tag.getTagId());

        if(oldTags.size() > 0) {
            session().createNamedQuery(ArticleQueries.REMOVE_ARTICLE_TAGS)
                    .setParameterList("tags", tagsToDelete).executeUpdate();
        }
        /*
        * Всё что осталось в новых тегах добавляем
        * */
        Set<ArticlesTags> tagsToAdd = new HashSet<>(newTags.size());
        for(int i = 0; i < newTags.size(); i++)
            tagsToAdd.add(new ArticlesTags(article, newTags.get(i)));

        return tagsToAdd;
    }


    /**
     * delete post with this
     * */
    @Override
    public boolean deleteArticle(long articleId, long userId) {

        try{
            Session session = session();
            UserInfo user = session.load(UserInfo.class, userId);

            return session.createNamedQuery(ArticleQueries.DELETE_ARTICLE)
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
            UserInfo user = session.load(UserInfo.class, userId);

            ArticleComments articleComments = new ArticleComments(comment, user, post);

            return session.createNamedQuery(ArticleQueries.GET_ARTICLE_COMMENT, CommonCommentTransfer.class)
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
            return session.createNamedQuery(ArticleQueries.UPDATE_ARTICLE_COMMENT)
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
            ArticleComments comment = session.load(ArticleComments.class, commentId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new ArticleCommentVoters(comment, user));

            return session.createNamedQuery(ArticleQueries.UPDATE_ARTICLE_COMMENT_RATE)
                    .setParameter("id", commentId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeComment(long commentId, long userId){
        try {
            Session session = session();
            return session.createNamedQuery(ArticleQueries.DELETE_ARTICLE_COMMENT)
                    .setParameter("id", commentId)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Long> getTagsByArticle(long articleId) {

        List<Long> tags = session().createNamedQuery(ArticleQueries.GET_TAGS_BY_ARTICLE, Long.class)
                .setParameter("id", articleId).getResultList();
        return tags != null ? tags : Collections.emptyList();
    }

    @Override
    public long getArticlesNum() {
        return (long) session().createNamedQuery(ArticleQueries.COUNT_ARTICLES, Object.class)
                .getSingleResult();
    }

    @Override
    public PageableEntity getSmallArticlesByTag(int start, int size, long tagID, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticlesTags> tags = article.join("tags", JoinType.LEFT);
        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        if(q != null)
            query.where(cb.and(cb.equal(tags.get("tagId"), tagID),
                    cb.or(cb.like(article.get("title"), "%"+q.replace(" ", "%")+"%"),
                            cb.like(content.get("subtitle"), "%"+q.replace(" ", "%")+"%"))));
        else
            query.where(cb.equal(tags.get("tagId"), tagID));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(ViewArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("middleImagePath")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity( session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList()
                , count);
    }

    @Override
    public PageableEntity getCommonArticlesByUser(int start, int size, long userId, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, UserInfo> user = article.join("userInfo", JoinType.LEFT);
        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        if(q != null)
            query.where(cb.and(cb.equal(user.get("userId"), userId),
                    cb.or(cb.like(article.get("title"), "%"+q.replace(" ", "%")+"%"),
                            cb.like(content.get("subtitle"), "%"+q.replace(" ", "%")+"%"))));
        else
            query.where(cb.equal(user.get("userId"), userId));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("middleImagePath"),
                article.get("createDate"), article.get("views"),article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getCommonArticlesByTag(int start, int size, long tagID, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<ArticleInfo> article = query.from(ArticleInfo.class);

        Join<ArticleInfo, ArticlesTags> tags = article.join("tags", JoinType.LEFT);
        Join<ArticleInfo, ArticleContent> content = article.join("articleContent", JoinType.LEFT);

        if(q != null)
            query.where(cb.and(cb.equal(tags.get("tagId"), tagID),
                    cb.or(cb.like(article.get("title"), "%"+q.replace(" ", "%")+"%"),
                            cb.like(content.get("subtitle"), "%"+q.replace(" ", "%")+"%"))));
        else
            query.where(cb.equal(tags.get("tagId"), tagID));

        query.select(cb.count(article.get("articleId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        if(start > count)
            return new PageableEntity(Collections.emptyList(), count);

        query.select(cb.construct(CommonArticleTransfer.class,
                article.get("articleId"), article.get("title"), article.get("middleImagePath"),
                article.get("createDate"), article.get("views"), article.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(article.get(orderField)));
        }else {
            query.orderBy(cb.desc(article.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }
}
