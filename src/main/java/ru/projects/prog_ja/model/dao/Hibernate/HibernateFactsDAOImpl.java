package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.model.dao.FactsDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.FactConverter;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.facts.FactsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Scope(scopeName = "prototype")
public class HibernateFactsDAOImpl extends GenericDAO implements FactsDAO {

    public static final String ID_COLUMN = "factId";
    public static final String ENTITIES_NAME = "facts";

    private final AttachTagService<FactsTags> attachTags;
    private final FactConverter factConverter;

    public HibernateFactsDAOImpl(@Autowired SessionFactory sessionFactory,
                                 @Autowired AttachTagService<FactsTags> attachTags,
                                 @Autowired FactConverter factConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.factConverter = factConverter;
    }

    @Override
    public CommonFactTransfer addFact(String fact, List<Long> tags, long userId) {

        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null){
                return null;
            }

            Facts facts = new Facts(fact);
            facts.setCreator(user);

            Set<FactsTags> factsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                factsTags.add(new FactsTags(facts, tag));
            });
            if(factsTags.size() < 3){
                return null;
            }
            facts.setTags(factsTags);

            return getFact((long) session.save(fact));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean deleteFact(long id, long userId) {

        try {

           return session().createNamedQuery(Facts.DELETE_FACT)
                    .setParameter("id", id)
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateFact(long id, String fact, List<Long> tags, long userId) {

        try {
            Session session = session();

            Facts facts = session.find(Facts.class, id);

            if(facts == null || facts.getCreator().getUserId() != userId)
                return false;
            facts.setText(fact);

            Set<FactsTags> factsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                factsTags.add(new FactsTags(facts, tag));
            });
            if(factsTags.size() < 3){
                return false;
            }
            facts.setTags(factsTags);

            session.save(facts);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateFactRate(long factId, int rate, long userId) {
        try{
            return session().createNamedQuery(Facts.UPDATE_FACT_RATE)
                    .setParameter("id", factId)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<CommonFactTransfer> getFacts(int start, int size, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> query = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> fact = query.from(Facts.class);

        query.select(cb.construct(CommonFactTransfer.class, fact.get("factId"), fact.get("text")));
        if(sort == 0){
            query.orderBy(cb.asc(fact.get(orderField)));
        }else{
            query.orderBy(cb.desc(fact.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonFactTransfer> findFacts(int start, int size, String query, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> cq = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> root = cq.from(Facts.class);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));
        cq.where(cb.like(root.get("text"), "%"+query.replace(" ", "%")+"%"));
        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return attachTags.tags(
                session.createQuery(cq).setFirstResult(start)
                .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonFactTransfer> getFactsByTag(int start, int size, long tagID, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> query = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> fact = query.from(Facts.class);

        Join<Facts, FactsTags> tags = fact.join("tags");

        query.select(cb.construct(CommonFactTransfer.class, fact.get("factId"), fact.get("text")));
        query.where(cb.equal(tags.get("tagId"), tagID));
        if(sort == 0){
            query.orderBy(cb.asc(fact.get(orderField)));
        }else{
            query.orderBy(cb.desc(fact.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonFactTransfer> findFactsByTag(int start, int size, long tagID, String query, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> cq = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> root = cq.from(Facts.class);

        Join<Facts, FactsTags> tagsJoin = root.join("tags", JoinType.LEFT);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));
        cq.where(cb.and(cb.like(root.get("text"),"%" +query.replace(" ", "%") + "%"),
                cb.equal(tagsJoin.get("tagId"), tagID)));
        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return attachTags.tags(
                session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonFactTransfer> getFactsByUser(int start, int size, long userId, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> cq = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> root = cq.from(Facts.class);

        Join<Facts, UserInfo> user = root.join("userInfo", JoinType.LEFT);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));
        cq.where(cb.equal(user.get("userId"), userId));
        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return attachTags.tags(
                session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonFactTransfer> findFactsByUser(int start, int size, long userId, String query, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> cq = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> root = cq.from(Facts.class);

        Join<Facts, UserInfo> user = root.join("user", JoinType.LEFT);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));
        cq.where(cb.and(cb.like(root.get("text"), "%"+query.replace(" ", "%")+"%"),
                cb.equal(user.get("userId"), user)));
        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return attachTags.tags(
                session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME);
    }


    @Override
    public List<CommonFactTransfer> getAllFacts(){

        return attachTags.tags(session().createNamedQuery( Facts.GET_COMMON_FACTS, CommonFactTransfer.class).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public long getFactsNum() {
        return (long) session().createNamedQuery(Facts.COUNT_FACTS, Object.class)
                .getSingleResult();
    }

    @Override
    public CommonFactTransfer getFact(long factId) {

        Facts fact = session().createNamedQuery(Facts.GET_FACT, Facts.class)
                .setParameter("id", factId)
                .getResultList().stream().findFirst()
                .orElse(null);
        if(fact == null){
            return null;
        }

        return factConverter.commonFact(fact, fact.getTags());
    }

    @Override
    public FullFactTransfer getFullFact(long factId) {

        return session().createNamedQuery(Facts.GET_FULL_FACT, FullFactTransfer.class)
                .setParameter("id", factId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public CommonFactTransfer getFactByTag(long tagID, long offset) {

        Session session = session();

        Facts fact = session.createNamedQuery(Facts.GET_FACT_BY_TAG, Facts.class)
                .setParameter("tag", session.load(Tags.class, tagID))
                .setFirstResult((int)offset)
                .getResultList().stream().findFirst().orElse(null);
        if(fact == null){
            return null;
        }

        return factConverter.commonFact(fact, fact.getTags());
    }

    @Override
    public long countFactsByTag(long tagId) {
        Session session = session();

        return (long) session.createNamedQuery(Facts.COUNT_FACTS_BY_TAG, Object.class)
                .setParameter("tag", session.load(Tags.class , tagId))
                .getSingleResult();
    }
}
