package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.AuthDTO;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.model.dao.AuthDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.UserConverter;
import ru.projects.prog_ja.model.dao.Hibernate.queries.UserQueries;
import ru.projects.prog_ja.model.dao.NoticeDAO;
import ru.projects.prog_ja.model.entity.user.*;


@Repository
@Scope("prototype")
public class HibernateAuthDAOImpl extends GenericDAO implements AuthDAO {


    public static String smallImagePath = "files/00/00/00/user-small.png";
    public static String middleImagePath = "files/00/00/00/user-middle.png";
    public static String largeImagePath = "files/00/00/00/user-large.png";

    private final UserConverter userConverter;
    private final NoticeDAO noticeDAO;

    @Autowired
    public HibernateAuthDAOImpl(SessionFactory sessionFactory,
                                UserConverter userConverter,
                                NoticeDAO noticeDAO) {
        super(sessionFactory);
        this.userConverter = userConverter;
        this.noticeDAO = noticeDAO;
    }

    @Override
    public UserDTO createUser(String login, String password_h, String email) {

        Session session = session();
        /*
         * Создаём юзера и присваиваем ему дефолтные изображения
         */
        UserInfo userInfo = new UserInfo(login, smallImagePath, middleImagePath, largeImagePath);

        /*
         * Создаём log info, содержащий хеши майла, пароля и соль
         * */
        LogInfo logInfo = new LogInfo(login, password_h, email, true);
        logInfo.setRole(new UserRoles(logInfo, Role.ROLE_NONACTIVE));
        userInfo.setLogInfo(logInfo);
        logInfo.setUserInfo(userInfo);

        UserExtended userExtended = new UserExtended( login, "",
                new java.sql.Date(new java.util.Date().getTime()), "", "");
        userInfo.setUserExtended(userExtended);
        userExtended.setUser(userInfo);

        UserCounter userCounter = new UserCounter();
        userInfo.setUserCounter(userCounter);
        userCounter.setUser(userInfo);

        try {
            session.save(userInfo);
        }catch (HibernateException e){
            return null;
        }

        return checkUser(login, password_h);
    }

    @Override
    public boolean saveActivateToken(String email, String token){

        try {

            Session session = session();

            Object[] logAndToken = (Object[]) session.createNamedQuery(UserQueries.GET_LOG_TOKEN_BY_EMAIL)
                    .setParameter("email", email)
                    .getResultList().stream().findFirst().orElse(new Long[]{(long)-1, null});

            if((long) logAndToken[0] == -1)
                return false;

            ActivateTokens activateToken
                    = new ActivateTokens(session.load(LogInfo.class,(long) logAndToken[0]), token);
            if(logAndToken[1] != null)
                activateToken.setActivateTokenId((long)logAndToken[1]);

            session.saveOrUpdate(activateToken);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public AuthDTO getAuthDTOByEmail(String email) {

        return session().createNamedQuery(UserQueries.GET_AUTH_BY_EMAIl, AuthDTO.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public AuthDTO getAuthDTOByUserId(long userId) {

        return session().createNamedQuery(UserQueries.GET_AUTH_BY_ID, AuthDTO.class)
                .setParameter("id", userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean isFreeLogin(String login){

                return session().createNamedQuery(UserQueries.CHECK_LOGIN, Long.class)
                .setParameter("login", login)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isFreeEmail(String email) {
        return session().createNamedQuery(UserQueries.GET_LOG_ID_BY_EMAIL, Long.class)
                .setParameter("email", email)
                .getResultList().isEmpty();
    }

    @Override
    public boolean activateAccount(String token) {

        try{
            Session session = session();

            return session.createNativeQuery("update logInfo l" +
                    " left outer join user_roles r on l.log_id = r.log_id " +
                    " left outer join activate_tokens t on l.log_id = t.log_id " +
                    " set r.role = 'ROLE_USER',  t.token = '' " +
                    " where t.token = '"+token+"'").executeUpdate() != 0;

        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public boolean saveToken(long userId, String salt, String token){

        try {
            Session session = session();

            UserInfo userInfo = session.load(UserInfo.class, userId);

            SecuredToken securedToken = session.createNamedQuery(UserQueries.GET_TOKEN_BY_USER, SecuredToken.class)
                    .setParameter("id", userId)
                    .getResultList().stream().findFirst().orElse(new SecuredToken());
            securedToken.setUser(userInfo);

            securedToken.setSalt(salt);
            securedToken.setToken(token);

            session.saveOrUpdate(securedToken);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public SecuredToken getTokenByUser(long userId){

        return session().createNamedQuery(UserQueries.GET_TOKEN_BY_USER, SecuredToken.class)
                .setParameter("id", userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public UserDTO getSmallUserByLogin(String name){

        Session session = session();
        UserInfo user = session.createNamedQuery(UserQueries.GET_SMALL_USER_BY_LOGIN, UserInfo.class)
                .setParameter("login", name)
                .getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }

        return userConverter.forumUser(user, user.getLogInfo().getRole().getRole(),
                user.getInterests(), session.createNamedQuery(UserQueries.COUNT_USER_NOTICES, Long.class)
                        .setParameter("id", user.getUserId())
        .getResultList().stream().findFirst().orElse((long) 0));
    }


    @Override
    public boolean updateEmail(long id, String email) {
        try{

            Session session = session();
            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_EMAIl)
                    .setParameter("email", email)
                    .setParameter("user", user)
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updatePass(String pass_h, long id){

        try {
            Session session = session();
            UserInfo user = session.load(UserInfo.class, id);

            return session.createNamedQuery(UserQueries.UPDATE_PASSWORD)
                    .setParameter("pass", pass_h)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public UserDTO checkUser(String login, String password_h) {

        Session session = session();
        UserInfo user = session.createNamedQuery(UserQueries.CHECK_USER, UserInfo.class)
                .setParameter("login", login)
                .setParameter("pass", password_h).getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }

        long notices = session.createNamedQuery(UserQueries.COUNT_USER_NOTICES, Long.class)
                .setParameter("id", user.getUserId())
                .getResultList().stream().findFirst().orElse((long) 0);

        return userConverter.forumUser(user, user.getLogInfo().getRole().getRole(),
                user.getInterests(), notices);
    }

    @Override
    public long getUserIdByEmail(String email) {

        return session().createNamedQuery(UserQueries.GET_USER_ID_BY_EMAIL, Long.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst().orElse((long) -1);
    }

    @Value("${image.user.small}")
    private void setSmallImagePath(String path){
        smallImagePath = path;
    }

    @Value("${image.user.middle}")
    private void setMiddleImagePath(String path){
        middleImagePath = path;
    }

    @Value("${image.user.large}")
    private void setLargeImagePath(String path){
        largeImagePath = path;
    }
}
