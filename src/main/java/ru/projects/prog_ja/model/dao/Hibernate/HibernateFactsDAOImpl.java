package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.model.dao.FactsDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.FactConverter;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.facts.FactsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Scope(scopeName = "prototype")
@Transactional(propagation = Propagation.REQUIRED)
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
    public long addFact(String fact, List<Long> tags, long userId) {

        Session session = session();

        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null){
            return 0;
        }

        Facts facts = new Facts(fact);
        facts.setCreator(user);

        Set<FactsTags> factsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            factsTags.add(new FactsTags(facts, tag));
        });
        if(factsTags.size() < 3){
            return 0;
        }
        facts.setTags(factsTags);

        return (long) session.save(facts);
    }

    @Override
    public void deleteFact(long id) {

        session().createNamedQuery(Facts.DELETE_FACT).setParameter("id", id).executeUpdate();

    }

    @Override
    public void updateFact(long id, String fact, List<Long> tags) {

        Session session = session();

        Facts facts = session.find(Facts.class, id);

        if(facts == null)
            return;
        facts.setText(fact);

        Set<FactsTags> factsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            factsTags.add(new FactsTags(facts, tag));
        });
        if(factsTags.size() < 3){
            return;
        }
        facts.setTags(factsTags);

        session.save(facts);
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
    public CommonFactTransfer getFactByTags(List<Long> tagsIDs){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonFactTransfer> query = cb.createQuery(CommonFactTransfer.class);
        Root<Facts> fact = query.from(Facts.class);

        Fetch<Facts, FactsTags> tagsFetch = fact.fetch("tags", JoinType.LEFT);
        Join<Facts, FactsTags> tags = fact.join("tags", JoinType.LEFT);

        Fetch<FactsTags, Tags> tag = fact.fetch("tagId", JoinType.LEFT);

        query.select(cb.construct(CommonFactTransfer.class, fact.get("factId"), fact.get("text")));

        List<Predicate> predicates = new ArrayList<>();
        for(long l : tagsIDs){
            predicates.add(cb.equal(tags.get("tagId"), session.load(Tags.class, l)));
        }
        query.where(cb.or((Predicate[]) predicates.toArray()));


        return attachTags.tags(session.createQuery(query).getResultList(),
                ID_COLUMN, ENTITIES_NAME).get(0);
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
