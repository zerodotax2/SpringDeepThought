package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.dao.TagsDAO;
import ru.projects.prog_ja.model.entity.tags.TagCounter;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
@Scope(scopeName = "prototype")
public class HibernateTagsDAOImpl extends GenericDAO implements TagsDAO {


    public HibernateTagsDAOImpl(@Autowired SessionFactory sessionFactory){
        super(sessionFactory);
    }

    @Override
    public boolean removeTag(long tagId, long userId) {
        try{
            Session session = session();
            UserInfo user = session.load(UserInfo.class, userId);

            return session.createNamedQuery(TagQueries.DELETE_TAG)
                    .setParameter("id", tagId)
                    .setParameter("user", user)
                    .executeUpdate() !=0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * create named query from tags class and get tag with all lists
     * */
    @Override
    public FullTagTransfer getFullTag(long id) {

        return session().createNamedQuery(TagQueries.GET_FULL_TAG, FullTagTransfer.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public String getName(long tagId) {
        return session().createNamedQuery(TagQueries.GET_NAME, String.class)
                .setParameter("id", tagId)
                .getResultList().stream().findFirst().orElse(null);
    }


    @Override
    public SmallTagTransfer getSmallTag(long id) {

        return session().createNamedQuery(TagQueries.GET_SMALL_TAG, SmallTagTransfer.class)
                .setParameter("id", id).stream().findFirst().orElse(null);
    }

    @Override
    public CommonTagTransfer getCommonTag(long id){

        return session().createNamedQuery(TagQueries.GET_COMMON_TAG, CommonTagTransfer.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);

    }

    @Override
    public CommonTagTransfer createTag(String name, String description, String color, long userId) {

        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            Tags tags = new Tags(name, description, color, user);

            TagCounter tagCounter = new TagCounter();
            tagCounter.setTag(tags);
            tags.setTagCounter(tagCounter);

            long id = (long) session.save(tags);
            return getCommonTag(id);
        }catch (Exception e){
            return null;
        }

    }


    @Override
    public boolean updateTag(long tagId, String name, String description, String color, long userId) {

        try{
            Session session = session();

            Tags tag = session.createNamedQuery(TagQueries.GET_UPDATE_TAG_ENTITY, Tags.class)
                    .setParameter("id", tagId)
                    .getResultList().stream().findFirst().orElse(null);
            if(tag == null || tag.getCreator().getUserId() != userId)
                return false;

            if(!tag.isActivated())
                return false;

            tag.setName(name);
            tag.setDescription(description);
            tag.setColor(color);

            session.save(tag);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public PageableEntity findTags(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags,TagCounter> counter = tag.join("tagCounter", JoinType.LEFT);

        query.where(cb.like(tag.get("name"), "%"+search.replace(" ", "%")+"%"));

        query.select(cb.count(tag.get("tagId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallTagTransfer.class, tag.get("tagId"), tag.get("name"), tag.get("color")));

        if(orderField.equals("rating")){
            if(sort==0)
                query.orderBy(cb.asc(counter.get("uses")));
            else
                query.orderBy(cb.desc(counter.get("uses")));
        }else{
            if(sort == 0)
                query.orderBy(cb.asc(tag.get(orderField)));
            else
                query.orderBy(cb.desc(tag.get(orderField)));

        }
        return new PageableEntity(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity getTagsByUser(int start, int size, long userId, String q, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, UserInfo> user = tag.join("creator", JoinType.LEFT);
        Join<Tags, TagCounter> counter = tag.join("tagCounter");

        if(q!=null)
            query.where(cb.and(cb.equal(user.get("userId"), userId),
                    cb.like(tag.get("name"), "%"+q.replace(" ", "%")+"%")));
        else
            query.where(cb.equal(user.get("userId"), userId));

        query.select(cb.count(tag.get("tagId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonTagTransfer.class,
                tag.get("tagId"), tag.get("name"), tag.get("color"), tag.get("description"),
                counter.get("articles"), counter.get("questions"), counter.get("problems")));

        if(orderField.equals("rating")){
            if(sort==0)
                query.orderBy(cb.asc(counter.get("uses")));
            else
                query.orderBy(cb.desc(counter.get("uses")));
        }else{
            if(sort == 0)
                query.orderBy(cb.asc(tag.get(orderField)));
            else
                query.orderBy(cb.desc(tag.get(orderField)));

        }

        return new PageableEntity(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity getCommonTags(int start, int size, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, TagCounter> counter = tag.join("tagCounter");

        query.select(cb.count(tag.get("tagId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonTagTransfer.class,
                tag.get("tagId"), tag.get("name"), tag.get("color"), tag.get("description"),
                counter.get("articles"), counter.get("questions"), counter.get("problems")));

        if(orderField.equals("rating")){
            if(sort==0)
                query.orderBy(cb.asc(counter.get("uses")));
            else
                query.orderBy(cb.desc(counter.get("uses")));
        }else{
            if(sort == 0)
                query.orderBy(cb.asc(tag.get(orderField)));
            else
                query.orderBy(cb.desc(tag.get(orderField)));

        }

        return new PageableEntity(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity findCommonTags(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, TagCounter> counter = tag.join("tagCounter");

        query.where(cb.or(cb.like(tag.get("name"), "%"+search.replace(" ", "%")+"%"),
                cb.like(tag.get("description"), "%"+search.replace(" ", "%"))));

        query.select(cb.count(tag.get("tagId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonTagTransfer.class,
                tag.get("tagId"), tag.get("name"), tag.get("color"), tag.get("description"),
                counter.get("articles"), counter.get("questions"), counter.get("problems")));

        if(orderField.equals("rating")){
            if(sort==0)
                query.orderBy(cb.asc(counter.get("uses")));
            else
                query.orderBy(cb.desc(counter.get("uses")));
        }else{
            if(sort == 0)
                query.orderBy(cb.asc(tag.get(orderField)));
            else
                query.orderBy(cb.desc(tag.get(orderField)));

        }

        return new PageableEntity(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity getSmallTags(int start, int size, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, TagCounter> counter = tag.join("tagCounter", JoinType.LEFT);

        query.select(cb.count(tag.get("tagId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallTagTransfer.class, tag.get("tagId"), tag.get("name"), tag.get("color")));

        if(orderField.equals("rating")){
            if(sort==0)
                query.orderBy(cb.asc(counter.get("uses")));
            else
                query.orderBy(cb.desc(counter.get("uses")));
        }else{
            if(sort == 0)
                query.orderBy(cb.asc(tag.get(orderField)));
            else
                query.orderBy(cb.desc(tag.get(orderField)));

        }

        return new PageableEntity(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommonTagTransfer> getAllTags() {

        return session().createNamedQuery(TagQueries.GET_COMMON_TAGS, CommonTagTransfer.class)
                .getResultList();

    }

    @Override
    @Transactional(readOnly = true)
    public long getTagsNum() {
        return (long) session().createNamedQuery(TagQueries.COUNT_TAGS, Object.class)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmallTagTransfer> getTagsByPrefix(String prefix, int size) {

        return session().createNamedQuery(TagQueries.GET_TAGS_BY_PREFIX, SmallTagTransfer.class)
                .setParameter("q", prefix + "%")
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmallTagTransfer> getSmallPopularTags(int start, int size) {

        return session().createNamedQuery(TagQueries.GET_SMALL_POPULAR_TAGS, SmallTagTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<CommonTagTransfer> getCommonPopularTags(int start, int size) {

        return session().createNamedQuery(TagQueries.GET_COMMON_POPULAR_TAGS, CommonTagTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
}
