package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.TagsDAO;
import ru.projects.prog_ja.model.entity.tags.TagCounter;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInbox;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
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
            if(user == null){
                return false;
            }

            return session.createNamedQuery(Tags.DELETE_TAG)
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

        return session().createNamedQuery(Tags.GET_FULL_TAG, FullTagTransfer.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }


    @Override
    public SmallTagTransfer getSmallTag(long id) {

        return session().createNamedQuery(Tags.GET_SMALL_TAG, SmallTagTransfer.class)
                .setParameter("id", id).stream().findFirst().orElse(null);
    }

    @Override
    public CommonTagTransfer getCommonTag(long id){

        return session().createNamedQuery(Tags.GET_COMMON_TAG, CommonTagTransfer.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);

    }

    @Override
    public CommonTagTransfer createTag(String name, String description, String color, long userId) {

        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null){
                return null;
            }

            Tags tags = new Tags(name, description, color, user);

            return getCommonTag((long) session().save(tags));
        }catch (Exception e){
            return null;
        }

    }


    @Override
    public boolean updateTag(long tagId, String name, String description, String color, long userId) {

        try{
            Tags tag = session().find(Tags.class, tagId);
            if(tag == null || tag.getCreator().getUserId() != userId)
                return false;

            if(!tag.isActivated())
                return false;

            tag.setName(name);
            tag.setDescription(description);
            tag.setColor(color);

            session().save(tag);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public List<SmallTagTransfer> findTags(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallTagTransfer> query = cb.createQuery(SmallTagTransfer.class);
        Root<Tags> tag = query.from(Tags.class);

        query.select(cb.construct(SmallTagTransfer.class, tag.get("tagId"), tag.get("name"), tag.get("color")));
        query.where(cb.like(tag.get("name"), "%"+search.replace(" ", "%")+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(tag.get(orderField)));
        }else{
            query.orderBy(cb.desc(tag.get(orderField)));
        }

       return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<CommonTagTransfer> getCommonTags(int start, int size, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonTagTransfer> query = cb.createQuery(CommonTagTransfer.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, TagCounter> tagCounter = tag.join("tagCounter");

        query.select(cb.construct(CommonTagTransfer.class,
                tag.get("tagId"), tag.get("name"), tag.get("color"), tag.get("description"),
                tagCounter.get("articles"), tagCounter.get("questions"), tagCounter.get("problems")));

        if(sort == 0){
            query.orderBy(cb.asc(tag.get(orderField)));
        }else{
            query.orderBy(cb.desc(tag.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<CommonTagTransfer> findCommonTags(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonTagTransfer> query = cb.createQuery(CommonTagTransfer.class);
        Root<Tags> tag = query.from(Tags.class);

        Join<Tags, TagCounter> tagCounter = tag.join("tagCounter");

        query.select(cb.construct(CommonTagTransfer.class,
                tag.get("tagId"), tag.get("name"), tag.get("color"), tag.get("description"),
                tagCounter.get("articles"), tagCounter.get("questions"), tagCounter.get("problems")));
        query.where(cb.or(cb.like(tag.get("name"), "%"+search.replace(" ", "%")+"%"),
                cb.like(tag.get("description"), "%"+search.replace(" ", "%"))));

        if(sort == 0){
            query.orderBy(cb.asc(tag.get(orderField)));
        }else{
            query.orderBy(cb.desc(tag.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<SmallTagTransfer> getSmallTags(int start, int size, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallTagTransfer> query = cb.createQuery(SmallTagTransfer.class);
        Root<Tags> tag = query.from(Tags.class);

        query.select(cb.construct(SmallTagTransfer.class, tag.get("tagId"), tag.get("name"), tag.get("color")));

        if(sort == 0){
            query.orderBy(cb.asc(tag.get(orderField)));
        }else{
            query.orderBy(cb.desc(tag.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<CommonTagTransfer> getAllTags() {

        return session().createNamedQuery(Tags.GET_COMMON_TAGS, CommonTagTransfer.class)
                .getResultList();

    }

    @Override
    public long getTagsNum() {
        return (long) session().createNamedQuery(Tags.COUNT_TAGS, Object.class)
                .getSingleResult();
    }

    @Override
    public List<SmallTagTransfer> getSmallPopularTags(int start, int size) {

        return session().createNamedQuery(Tags.GET_SMALL_POPULAR_TAGS, SmallTagTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<CommonTagTransfer> getCommonPopularTags(int start, int size) {

        return session().createNamedQuery(Tags.GET_COMMON_POPULAR_TAGS, CommonTagTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
}
