package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.UserConverter;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.UserQueries;
import ru.projects.prog_ja.model.dao.UserDAO;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.*;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.*;


@Repository
@Scope(scopeName = "prototype")
public class HibernateUserDAOImpl extends GenericDAO implements UserDAO {

    private static final String ID_COLUMN = "userId";
    private static final String ENTITIES_NAME = "users";

    private final UserConverter userConverter;

    public HibernateUserDAOImpl(@Autowired SessionFactory sessionFactory,
                                @Autowired UserConverter userConverter){
        super(sessionFactory);
        this.userConverter = userConverter;
    }


    @Override
    public PageableEntity getSmallUsers(int start, int size, String orderField, int sort){

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public CommonUserTransfer getCommonUser(long id){

        return session().createNamedQuery(UserQueries.GET_COMMON_USER, CommonUserTransfer.class)
                .setParameter("id", id).stream().findFirst().orElse(null);
    }

    @Override
    public FullUserTransfer getFullUser(long id) {

        UserInfo user = session().createNamedQuery(UserQueries.GET_FULL_USER, UserInfo.class)
                .setParameter("id", id).stream().findFirst().orElse(null);

        if(user == null){
            return null;
        }

        return userConverter.fullUser(user, user.getUserExtended(), user.getUserCounter(), user.getInterests());
    }

    @Override
    public PageableEntity findUsers(int start, int size, String search, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        query.where(cb.like(user.get("login"), "%"+search+"%"));

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity findCommonUsers(int start, int size, String search, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UserExtended> userExtended = user.join("userExtended", JoinType.LEFT);

        query.where(cb.like(user.get("login"), "%"+search+"%"));

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating"),
                userExtended.get("firstName"), userExtended.get("lastName"), userExtended.get("createDate"), userExtended.get("birthDate")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity getCommonUsers(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UserExtended> userExtended = user.join("userExtended", JoinType.LEFT);

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating"),
                userExtended.get("firstName"), userExtended.get("lastName"), userExtended.get("createDate"), userExtended.get("birthDate")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }


    @Override
    public PageableEntity getModers(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, LogInfo> logInfo = user.join("logInfo");
        Join<LogInfo, UserRoles> role = logInfo.join("role");

        query.where(cb.or(cb.equal(role.get("role"), Role.ROLE_MODER), cb.equal(role.get("role"), Role.ROLE_ADMIN)));

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity findModers(int start, int size, String search, String orderField,  int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, LogInfo> logInfo = user.join("logInfo");
        Join<LogInfo, UserRoles> role = logInfo.join("role");

        query.where(cb.and(cb.like(user.get("login"), "%"+search+"%"),
                cb.or(cb.equal(role.get("role"), Role.ROLE_MODER), cb.equal(role.get("role"), Role.ROLE_ADMIN))));

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public PageableEntity getSmallUsersByTag(int start, int size, long tagID, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UsersTags> tags = user.join("interests");

        if(q != null)
            query.where(cb.and(cb.equal(tags.get("tagId"), tagID),
                    cb.like(user.get("login"), "%"+q.replace(" ", "%")+"%")));
        else
            query.where(cb.equal(tags.get("tagId"), tagID));

        query.select(cb.count(user.get("userId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return new PageableEntity(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                count);
    }

    @Override
    public boolean updateUser(long id, String firstName, String lastName, Date birthDate, String bgImage, String about, List<Long> tags) {

        try{
            Session session = session();

            List<UserInfo> userInfos = session.createNamedQuery(UserQueries.GET_COMMON_USER, UserInfo.class)
                    .setParameter("id", id)
                    .getResultList();
            if(userInfos.size() != 1)
                return false;

            UserInfo userInfo = userInfos.get(0);

            userInfo.getUserExtended().setFirstName(firstName);
            userInfo.getUserExtended().setLastName(lastName);
            userInfo.getUserExtended().setBirthDate(birthDate);
            userInfo.getUserExtended().setAbout(about);
            userInfo.getUserExtended().setBgImage(bgImage);

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            userInfo.setInterests(updateTags(userInfo, tagsList));

            session.save(userInfo);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private Set<UsersTags> updateTags(UserInfo user, List<Tags> newTags){

        Set<UsersTags> oldTags = user.getInterests();

        if(newTags == null || newTags.size() < 3){
            return oldTags;
        }

        /*
         * Перебираем по новым тегам, если есть совпадение удаляем из старых и новых тегов
         * */
        for(Iterator<Tags> i = newTags.iterator(); i.hasNext();){

            Tags t = i.next();

            for(Iterator<UsersTags> j = oldTags.iterator(); j.hasNext();){
                UsersTags old = j.next();
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
        for(UsersTags tag: oldTags)
            tagsToDelete.add(tag.getTagId());
        if(oldTags.size() > 0)
            session().createNamedQuery(UserQueries.REMOVE_USER_TAGS)
                .setParameterList("tags", tagsToDelete).executeUpdate();

        /*
         * Всё что осталось в новых тегах добавляем
         * */
        Set<UsersTags> tagsToAdd = new HashSet<>(newTags.size());
        for(int i = 0; i < newTags.size(); i++)
            tagsToAdd.add(new UsersTags(user, newTags.get(i)));

        return tagsToAdd;
    }


    @Override
    public boolean updateBGImage(long id, String imagePath){
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_BGIMAGE)
                    .setParameter("image", imagePath)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateFirstName(long id, String firstName) {
        try{
            Session session = session();

            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_FIRSTNAME)
                 .setParameter("firstName", firstName)
                 .setParameter("user", user)
                 .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateLastName(long id, String lastName) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_LASTNAME)
                    .setParameter("lastName", lastName)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateAbout(long id, String about) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_ABOUT)
                 .setParameter("about", about)
                 .setParameter("user", user)
                 .executeUpdate() !=0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateInterests(long id, List<Long> tags) {

        try {
            Session session = session();

            UserInfo userInfo = session.find(UserInfo.class, id);
            if(userInfo == null){
                return false;
            }

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            userInfo.setInterests(updateTags(userInfo, tagsList));

            session.save(userInfo);
        }catch (Exception e){
            return  false;
        }

        return true;
    }

    @Override
    public boolean updateBirthdate(long id, Date date) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_BIRTHDATE)
                    .setParameter("user", user)
                    .setParameter("date", date)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateUserRate(long userId, int rate) {
        try {
            return session().createNamedQuery(UserQueries.UPDATE_RATE)
                    .setParameter("id", userId)
                    .setParameter("rate",(long) rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getUsername(long userId) {
        return session().createNamedQuery(UserQueries.GET_USERNAME, String.class)
                .setParameter("id", userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<SmallTagTransfer> getUserInterests(long userId) {
        return session().createNamedQuery(UserQueries.GET_USER_INTERESTS, SmallTagTransfer.class)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    public List<Long> getTagsByUser(long userId) {

        return session().createNamedQuery(UserQueries.GET_TAGS_BY_USER, Long.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    public boolean updateImage(long id, List<String> mainImages){

        try {
            return session().createNamedQuery(UserQueries.UPDATE_IMAGE)
                    .setParameter("small", mainImages.get(0))
                    .setParameter("middle", mainImages.get(1))
                    .setParameter("large", mainImages.get(2))
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }
    @Override
    public boolean deleteUser(long id) {
        try {

          return session().createNamedQuery(UserQueries.DELETE_USER)
                  .setParameter("id", id)
                  .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }



    @Override
    public long getUsersNum() {
        return (long) session().createNamedQuery(UserQueries.COUNT_USERS, Object.class)
                .getSingleResult();
    }
}
