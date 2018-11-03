package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.dao.AuthDAO;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.UserConverter;
import ru.projects.prog_ja.model.entity.user.*;

import java.sql.Date;

@Repository
@Scope("prototype")
public class HibernateAuthDAOImpl extends GenericDAO implements AuthDAO {

    @Value("${image.user.small}")
    public static String smallImagePath;

    @Value("${image.user.middle}")
    public static String middleImagePath;

    @Value("${image.user.large}")
    public static String largeImagePath;

    private final UserConverter userConverter;

    @Autowired
    public HibernateAuthDAOImpl(SessionFactory sessionFactory,
                                UserConverter userConverter) {
        super(sessionFactory);
        this.userConverter = userConverter;
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
        LogInfo logInfo = new LogInfo(login, password_h, true);
        logInfo.setRole(new UserRoles(logInfo, Role.ROLE_USER));
        userInfo.setLogInfo(logInfo);

        UserExtended userExtended = new UserExtended(email, "Mr", "Anonymous", new Date(new java.util.Date().getTime()), "", "");
        userInfo.setUserExtended(userExtended);

        session.save(userInfo);

        return checkUser(login, password_h);
    }

    @Override
    public boolean saveToken(long userId, String salt, String token){

        try {
            Session session = session();

            SecuredToken securedToken = new SecuredToken(salt, token);
            UserInfo userInfo = session.find(UserInfo.class, userId);
            if(userInfo == null){
                return false;
            }

            userInfo.setSecuredToken(securedToken);

            session.save(userInfo);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public SecuredToken getTokenByUser(long userId){

        return session().createNamedQuery(SecuredToken.GET_TOKEN_BY_USER, SecuredToken.class)
                .setParameter("id", userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public UserDTO getSmallUserByLogin(String name){

        Session session = session();
        UserInfo user = session.createNamedQuery(UserInfo.GET_SMALL_USER_BY_LOGIN, UserInfo.class)
                .setParameter("login", name)
                .getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }

        return userConverter.forumUser(user, user.getLogInfo().getRole().getRole(),
                user.getInterests(), user.getNotices());
    }


    @Override
    public boolean updateEmail(long id, String email) {
        try{

            return session().createNamedQuery(UserInfo.UPDATE_EMAIl)
                    .setParameter("email", email)
                    .setParameter("id", id)
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }
    @Override
    public boolean updateLogin(String login,  long id){

        try {

            return session().createSQLQuery("update logInfo l " +
                    " left outer join userInfo u on l.log_id = u.log_id " +
                    " set l.login = '"+login+"', " +
                    " u.login = '" + login + "' " +
                    " where u.user_id = '" + id +"'").executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updatePass(String pass_h, long id){

        try {
            return session().createNamedQuery(LogInfo.UPDATE_PASSWORD)
                    .setParameter("pass", pass_h)
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public UserDTO checkUser(String login, String password_h) {

        Session session = session();
        UserInfo user = session.createNamedQuery(UserInfo.CHECK_USER, UserInfo.class)
                .setParameter("login", login)
                .setParameter("pass", password_h).getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }

        return userConverter.forumUser(user, user.getLogInfo().getRole().getRole(),
                user.getInterests(), user.getNotices());
    }

}
