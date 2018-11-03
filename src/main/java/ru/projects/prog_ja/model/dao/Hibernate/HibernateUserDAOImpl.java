package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.UserConverter;
import ru.projects.prog_ja.model.dao.UserDAO;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.*;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    public List<SmallUserTransfer> getSmallUsers(int start, int size, String orderField, int sort){

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public CommonUserTransfer getCommonUser(long id){

        return session().createNamedQuery(UserInfo.GET_COMMON_USER, CommonUserTransfer.class)
                .setParameter("id", id).stream().findFirst().orElse(null);
    }

    @Override
    public FullUserTransfer getFullUser(long id) {

        UserInfo user = session().createNamedQuery(UserInfo.GET_FULL_USER, UserInfo.class)
                .setParameter("id", id).stream().findFirst().orElse(null);

        if(user == null){
            return null;
        }

        return userConverter.fullUser(user, user.getUserExtended(), user.getUserCounter(), user.getInterests());
    }

    @Override
    public List<SmallUserTransfer> findUsers(int start, int size, String search, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.like(user.get("login"), "%"+search+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<CommonUserTransfer> findCommonUsers(int start, int size, String search, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonUserTransfer> query = cb.createQuery(CommonUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UserExtended> userExtended = user.join("userExtended", JoinType.LEFT);

        query.select(cb.construct(CommonUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating"),
                userExtended.get("firstName"), userExtended.get("lastName"), userExtended.get("createDate"), userExtended.get("birthDate")));
        query.where(cb.like(user.get("login"), "%"+search+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<CommonUserTransfer> getCommonUsers(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonUserTransfer> query = cb.createQuery(CommonUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UserExtended> userExtended = user.join("userExtended", JoinType.LEFT);

        query.select(cb.construct(CommonUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating"),
                userExtended.get("firstName"), userExtended.get("lastName"), userExtended.get("createDate"), userExtended.get("birthDate")));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }


    @Override
    public List<SmallUserTransfer> getModers(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, LogInfo> logInfo = user.join("logInfo");
        Join<LogInfo, UserRoles> role = logInfo.join("role");

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.or(cb.equal(role.get("role"), Role.ROLE_MODER), cb.equal(role.get("role"), Role.ROLE_ADMIN)));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<SmallUserTransfer> findModers(int start, int size, String search, String orderField,  int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, LogInfo> logInfo = user.join("logInfo");
        Join<LogInfo, UserRoles> role = logInfo.join("role");

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.and(cb.like(user.get("login"), "%"+search+"%"),
                cb.or(cb.equal(role.get("role"), Role.ROLE_MODER), cb.equal(role.get("role"), Role.ROLE_ADMIN))));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<SmallUserTransfer> getSmallUsersByTag(int start, int size, long tagID, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, UsersTags> tags = user.join("interests");

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.equal(tags.get("tagId"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(user.get(orderField)));
        }else{
            query.orderBy(cb.desc(user.get(orderField)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public boolean updateUser(long id, String firstName, String lastName, Date birthDate, String bgImage, String about, List<Long> tags) {

        try{
            Session session = session();

            List<UserInfo> userInfos = session.createNamedQuery(UserInfo.GET_COMMON_USER, UserInfo.class)
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

            Set<UsersTags> usersTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
                usersTags.add(new UsersTags(userInfo, tag));
            });
            userInfo.setInterests(usersTags);

            session.save(userInfo);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateBGImage(long id, String imagePath){
        try {
           return session().createNamedQuery(UserInfo.UPDATE_BGIMAGE)
                    .setParameter("image", imagePath)
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateFirstName(long id, String firstName) {
        try{
         return session().createNamedQuery(UserInfo.UPDATE_FIRSTNAME)
                 .setParameter("firstName", firstName)
                 .setParameter("id", id)
                 .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateLastName(long id, String lastName) {
        try {
            return session().createNamedQuery(UserInfo.UPDATE_LASTNAME)
                    .setParameter("lastName", lastName)
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateAbout(long id, String about) {
        try {
         return session().createNamedQuery(UserInfo.UPDATE_ABOUT)
                 .setParameter("about", about)
                 .setParameter("id", id)
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

            Set<UsersTags> usersTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
                usersTags.add(new UsersTags(userInfo, tag));
            });
            userInfo.setInterests(usersTags);

            session.save(userInfo);
        }catch (Exception e){
            return  false;
        }

        return true;
    }




    @Override
    public boolean updateImage(long id, List<String> mainImages){

        try {
            return session().createNamedQuery(UserInfo.UPDATE_IMAGE)
                    .setParameter("small", mainImages.get(0))
                    .setParameter("middle", mainImages.get(1))
                    .setParameter("large", mainImages.get(2))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }
    @Override
    public boolean deleteUser(long id) {
        try {

          return session().createNamedQuery(UserInfo.DELETE_USER)
                  .setParameter("id", id)
                  .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }



    @Override
    public long getUsersNum() {
        return (long) session().createNamedQuery(UserInfo.COUNT_USERS, Object.class)
                .getSingleResult();
    }
}
