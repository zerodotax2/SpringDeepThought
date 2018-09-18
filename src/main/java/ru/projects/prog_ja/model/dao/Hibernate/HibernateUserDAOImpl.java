package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateUserDAOImpl extends GenericDAO implements UserDAO {

    private static final String ID_COLUMN = "userId";
    private static final String ENTITIES_NAME = "users";

    private final UserConverter userConverter;

    @Value("${image.user.small}")
    public static String smallImagePath;

    @Value("${image.user.middle}")
    public static String middleImagePath;

    @Value("${image.user.large}")
    public static String largeImagePath;

    public HibernateUserDAOImpl(@Autowired SessionFactory sessionFactory,
                                @Autowired UserConverter userConverter){
        super(sessionFactory);
        this.userConverter = userConverter;
    }

    @Override
    public SmallUserTransfer createUser(String login_h, String password_h, String email) {

        /*
        * Создаём юзера и присваиваем ему дефолтные изображения
        */
        UserInfo userInfo = new UserInfo(login_h, smallImagePath, middleImagePath, largeImagePath);

        /*
        * Создаём log info, содержащий хеши майла, пароля и соль
        * */
        LogInfo logInfo = new LogInfo(login_h, password_h, true);
        userInfo.setLogInfo(logInfo);

        UserExtended userExtended = new UserExtended(email, "", "", new Date(new java.util.Date().getTime()), "", "");
        userInfo.setUserExtended(userExtended);

        session().save(userInfo);

        return userConverter.smallUser(userInfo);
    }

    @Override
    public SmallUserTransfer checkUser(String login_h, String password_h) {

        return session().createNamedQuery(UserInfo.CHECK_USER, SmallUserTransfer.class)
                .setParameter("login", login_h)
                .setParameter("pass", password_h).getResultList().stream().findFirst().orElse(null);
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
    public List<SmallUserTransfer> getModers(int start, int size, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallUserTransfer> query = cb.createQuery(SmallUserTransfer.class);
        Root<UserInfo> user = query.from(UserInfo.class);

        Join<UserInfo, LogInfo> logInfo = user.join("logInfo");
        Join<LogInfo, UserRoles> role = logInfo.join("role");

        query.select(cb.construct(SmallUserTransfer.class, user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating")));
        query.where(cb.or(cb.equal(role.get("role"), Role.ROLE_MODER), cb.equal(role.get("role"), Role.ROLE_ADMIN)));

        if(sort == 0){
            query.orderBy(cb.asc(user.get("rating")));
        }else{
            query.orderBy(cb.desc(user.get("rating")));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public List<SmallUserTransfer> findModers(int start, String search, int size, int sort) {
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
            query.orderBy(cb.asc(user.get("rating")));
        }else{
            query.orderBy(cb.desc(user.get("rating")));
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
    public void updateUser(long id, String email, String firstName, String lastName, Date birthDate, String bgImage, String about, List<Long> tags) {

        Session session = session();

        List<UserInfo> userInfos = session.createNamedQuery(UserInfo.GET_COMMON_USER, UserInfo.class)
                .setParameter("id", id)
                    .getResultList();
        if(userInfos.size() != 1)
            return;

        UserInfo userInfo = userInfos.get(0);

        userInfo.getUserExtended().setEmail(email);
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
    }

    @Override
    public void updateBGImage(long id, String imagePath){

        session().createNamedQuery(UserInfo.UPDATE_BGIMAGE)
                    .setParameter("image", imagePath)
                    .setParameter("id", id)
                    .executeUpdate();
    }

    @Override
    public void updateFirstName(long id, String firstName) {
        session().createNamedQuery(UserInfo.UPDATE_FIRSTNAME)
                .setParameter("firstName", firstName)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateLastName(long id, String lastName) {
        session().createNamedQuery(UserInfo.UPDATE_LASTNAME)
                .setParameter("lastName", lastName)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateAbout(long id, String about) {
        session().createNamedQuery(UserInfo.UPDATE_ABOUT)
                .setParameter("about", about)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateInterests(long id, List<Long> tags) {

        Session session = session();

        UserInfo userInfo = session.find(UserInfo.class, id);
        if(userInfo == null){
            return;
        }

        Set<UsersTags> usersTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
            usersTags.add(new UsersTags(userInfo, tag));
        });
        userInfo.setInterests(usersTags);

        session.save(userInfo);
    }



    @Override
    public void updateLogin(String login_h, String login,  long id){

            session().createSQLQuery("update logInfo l " +
                    " left outer join userInfo u on l.log_id = u.log_id " +
                    " set l.login_h = '"+login_h+"', " +
                    " u.login = '" + login + "' " +
                    " where u.user_id = '" + id +"'").executeUpdate();

    }

    @Override
    public void updatePass(String pass_h, long id){

            session().createNamedQuery(LogInfo.UPDATE_PASSWORD)
                    .setParameter("pass", pass_h)
                    .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public void updateImage(long id, List<String> mainImages){

        session().createNamedQuery(UserInfo.UPDATE_IMAGE)
                .setParameter("small", mainImages.get(0))
                .setParameter("middle", mainImages.get(1))
                .setParameter("large", mainImages.get(2))
                .executeUpdate();
    }

    @Override
    public void updateEmail(long id, String email) {
        session().createNamedQuery(UserInfo.UPDATE_EMAIl)
                .setParameter("email", email)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteUser(long id) {

        session().createNamedQuery(UserInfo.DELETE_USER).setParameter("id", id).executeUpdate();

    }

    @Override
    public SmallUserTransfer getSmallUserByLogin(String name){

        return session().createNamedQuery(UserInfo.GET_SMALL_USER_BY_LOGIN, SmallUserTransfer.class)
                .setParameter("login", name)
                .setMaxResults(1)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean saveToken(long userId, String salt, String token){

        Session session = session();

        SecuredToken securedToken = new SecuredToken(salt, token);
        UserInfo userInfo = session.find(UserInfo.class, userId);
        if(userInfo == null){
            return false;
        }

        userInfo.setSecuredToken(securedToken);

        session.save(userInfo);

        return true;
    }

    @Override
    public SecuredToken getTokenByUser(long userId){

        return session().createNamedQuery(SecuredToken.GET_TOKEN_BY_USER, SecuredToken.class)
                .setParameter("id", userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public long getUsersNum() {
        return (long) session().createNamedQuery(UserInfo.COUNT_USERS, Object.class)
                .getSingleResult();
    }
}
