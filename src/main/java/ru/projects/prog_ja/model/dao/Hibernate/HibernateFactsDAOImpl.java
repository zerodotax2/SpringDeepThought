package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.FactsDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.FactConverter;
import ru.projects.prog_ja.model.dao.Hibernate.queries.FactQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.facts.FactsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
public class HibernateFactsDAOImpl extends GenericDAO implements FactsDAO {

    public static final String ID_COLUMN = "factId";
    public static final String ENTITIES_NAME = "facts";

    private final AttachTagService<FactsTags, Facts> attachTags;
    private final FactConverter factConverter;

    @Autowired
    public HibernateFactsDAOImpl(SessionFactory sessionFactory,
                                 AttachTagService<FactsTags, Facts> attachTags,
                                 FactConverter factConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.factConverter = factConverter;
    }

    @Override
    public CommonFactTransfer addFact(String fact, List<Long> tags, long userId) {

        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            Facts facts = new Facts(fact);
            facts.setCreator(user);

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            if(tagsList == null || tagsList.size() < 2){
                return null;
            }
            Set<FactsTags> tagsSet = new HashSet<>(tagsList.size());
            tagsList.forEach(tag -> tagsSet.add(new FactsTags(facts, tag)));
            facts.setTags(tagsSet);

            long id = (long) session.save(facts);
            return getFact(id);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getText(long factId) {
        return session().createNamedQuery(FactQueries.GET_TEXT, String.class)
                .setParameter("id", factId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeEntityTemplateDTO getNoticeTemplate(long factId) {

        return session().createNamedQuery(FactQueries.GET_NOTICE_TEMPLATE, NoticeEntityTemplateDTO.class)
                .setParameter("id", factId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Long> getTagsByFact(long factId) {

        return  session().createNamedQuery(FactQueries.GET_TAGS_BY_FACT, Long.class)
                .setParameter("id", factId).getResultList();
    }

    @Override
    public boolean deleteFact(long id, long userId) {

        try {

           return session().createNamedQuery(FactQueries.DELETE_FACT)
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

            Facts facts = session.createNamedQuery(FactQueries.GET_UPDATE_FACT_ENTITY, Facts.class)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst().orElse(null);

            if(facts == null || facts.getCreator().getUserId() != userId)
                return false;
            facts.setText(fact);

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            facts.setTags(updateTags(facts, tagsList));

            session.save(facts);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private Set<FactsTags> updateTags(Facts fact, List<Tags> newTags){

        Set<FactsTags> oldTags = fact.getTags();

        if(newTags == null || newTags.size() < 2){
            return Collections.emptySet();
        }

        /*
         * Перебираем по новым тегам, если есть совпадение удаляем из старых и новых тегов
         * */
        for(Iterator<Tags> i = newTags.iterator(); i.hasNext();){

            Tags t = i.next();

            for(Iterator<FactsTags> j = oldTags.iterator(); j.hasNext();){
                FactsTags old = j.next();
                if(old.getTagId().getTagId() == t.getTagId()){
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
        for(FactsTags tag: oldTags)
            tagsToDelete.add(tag.getTagId());
        if(oldTags.size() > 0)
            session().createNamedQuery(FactQueries.REMOVE_FACT_TAGS)
                .setParameterList("tags", tagsToDelete).executeUpdate();

        /*
         * Всё что осталось в новых тегах добавляем
         * */
        Set<FactsTags> tagsToAdd = new HashSet<>(newTags.size());
        for(int i = 0; i < newTags.size(); i++)
            tagsToAdd.add(new FactsTags(fact, newTags.get(i)));

        return tagsToAdd;
    }

    @Override
    public boolean updateFactRate(long factId, int rate, long userId) {
        try{
            Session session = session();
            return session.createNamedQuery(FactQueries.UPDATE_FACT_RATE)
                    .setParameter("id", factId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public PageableEntity getFacts(int start, int size, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Facts> fact = query.from(Facts.class);

        query.select(cb.count(fact.get("factId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonFactTransfer.class, fact.get("factId"), fact.get("text")));
        if(sort == 0){
            query.orderBy(cb.asc(fact.get(orderField)));
        }else{
            query.orderBy(cb.desc(fact.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity findFacts(int start, int size, String query, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);
        Root<Facts> root = cq.from(Facts.class);

        cq.where(cb.like(root.get("text"), "%"+query.replace(" ", "%")+"%"));

        cq.select(cb.count(root.get("factId")));
        long count = (Long) session.createQuery(cq).getResultList().stream().findFirst().orElse((long)0);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));

        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME), count);
    }


    @Override
    public PageableEntity getFactsByTag(int start, int size, long tagID, String query, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);
        Root<Facts> root = cq.from(Facts.class);

        Join<Facts, FactsTags> tagsJoin = root.join("tags", JoinType.LEFT);

        if(query != null)
            cq.where(cb.and(cb.like(root.get("text"),"%" +query.replace(" ", "%") + "%"),
                    cb.equal(tagsJoin.get("tagId"), tagID)));
        else
            cq.where(cb.equal(tagsJoin.get("tagId"), tagID));

        cq.select(cb.count(root.get("factId")));
        long count = (Long) session.createQuery(cq).getResultList().stream().findFirst().orElse((long)0);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));

        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME), count);
    }


    @Override
    public PageableEntity getFactsByUser(int start, int size, long userId, String query, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);
        Root<Facts> root = cq.from(Facts.class);

        Join<Facts, UserInfo> user = root.join("creator", JoinType.LEFT);

        if(query != null)
            cq.where(cb.and(cb.like(root.get("text"), "%"+query.replace(" ", "%")+"%"),
                    cb.equal(user.get("userId"), userId)));
        else
            cq.where(cb.equal(user.get("userId"), userId));

        cq.select(cb.count(root.get("factId")));
        long count = (Long) session.createQuery(cq).getResultList().stream().findFirst().orElse((long)0);

        cq.select(cb.construct(CommonFactTransfer.class, root.get("factId"), root.get("text")));

        if(sort == 0){
            cq.orderBy(cb.asc(root.get(orderField)));
        }else{
            cq.orderBy(cb.desc(root.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List) session.createQuery(cq).setFirstResult(start)
                        .setMaxResults(size).getResultList(), ID_COLUMN, ENTITIES_NAME), count);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CommonFactTransfer> getAllFacts(){

        return attachTags.tags(session().createNamedQuery(FactQueries.GET_COMMON_FACTS, CommonFactTransfer.class).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    @Transactional(readOnly = true)
    public long getFactsNum() {
        return (long) session().createNamedQuery(FactQueries.COUNT_FACTS, Object.class)
                .getSingleResult();
    }

    @Override
    public CommonFactTransfer getFact(long factId) {

        Facts fact = session().createNamedQuery(FactQueries.GET_FACT, Facts.class)
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

        Session session = session();

        FullFactTransfer fact =  session.createNamedQuery(FactQueries.GET_FULL_FACT, FullFactTransfer.class)
                .setParameter("id", factId)
                .getResultList().stream().findFirst().orElse(null);
        fact.setTags(new HashSet<>(getTagsByFactID(factId)));

        return fact;
    }

    @Override
    public List<SmallTagTransfer> getTagsByFactID(long factId){

        Session session = session();

        return session.createNamedQuery(FactQueries.GET_TAGS_BY_FACT_ID, SmallTagTransfer.class)
                .setParameter("fact", session.load(Facts.class, factId))
                .getResultList();
    }

    @Override
    public CommonFactTransfer getFactByTag(long tagID, long offset) {

        Session session = session();

        Facts fact = session.createNamedQuery(FactQueries.GET_FACT_BY_TAG, Facts.class)
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

        return (long) session.createNamedQuery(FactQueries.COUNT_FACTS_BY_TAG, Object.class)
                .setParameter("tag", session.load(Tags.class , tagId))
                .getSingleResult();
    }
}
