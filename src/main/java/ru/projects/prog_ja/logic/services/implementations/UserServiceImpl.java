package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.types.UserViewTypes;
import ru.projects.prog_ja.logic.services.interfaces.TokenService;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.logic.util.CookieUtil;
import ru.projects.prog_ja.logic.util.HashType;
import ru.projects.prog_ja.logic.util.HasherClass;
import ru.projects.prog_ja.model.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(scopeName = "prototype")
public class UserServiceImpl implements UserService {

    private CookieUtil cookieUtil;

    @Value("${users.small.show.size}")
    private int usersSmallSize;

    @Value("${users.common.show.size}")
    private int usersCommonSize;

    private final UserDAO userDAO;
    private TokenService tokenService;

    public UserServiceImpl(@Autowired UserDAO userDAO,
                           @Autowired TokenService tokenService){
        this.userDAO = userDAO;
        this.tokenService = tokenService;
        cookieUtil = new CookieUtil();
    }

    /**
     * remove user cookies
     * */
    @Override
    public void removeCookies(HttpServletRequest request, HttpServletResponse response){

        cookieUtil.removeCookies(request, response);
    }

    /**
     * checking if mail_h and pass_h in cookies exist
     * if yes then check user with this mail_g and pass_h
     * and return founded
     * */
    @Override
    public SmallUserTransfer checkLogPassInCookies(HttpServletRequest request) {

        Map<String, String> cookieMap = cookieUtil.findCookies(request);

        if(!cookieMap.containsKey("login_h") || !cookieMap.containsKey("password_h"))
            return null;

        return checkUser(cookieMap.get("login_h"),
                cookieMap.get("password_h"), false, null);
    }

    /**
     * adding email_h and pass_h in cookies
     * */
    @Override
    public void addLogPassCookies(String login_h, String pass_h, HttpServletResponse response) {
        /*
            Добавляем в куки пользователя email и пароль, для того, чтобы
            не приходилось каждый раз логиниться на сайт
        */
        if(response == null){
            return;
        }

        cookieUtil.addCookies(new HashMap<String, String>(){{
            put("login_h", login_h);
            put("password_h", pass_h);
        }}, response);
    }


    /**
     * create new user with this params
     * @param email - create SHA-256 hash for LogInfo and add in UserInfo
     * @param password create bcrypt hash for LogInfo
     * @param login - login for UserInfo
     *
     * Also create default user image for UserImages()
     * */
    @Override
    public SmallUserTransfer createUser(String login, String password, String email,
                                        boolean createCookie, HttpServletResponse response) {


        /*
        * Создаём SHA_256 хеш от логина и пароля, а также соль
        * */
        HasherClass hasher = new HasherClass( HashType.BCRYPT );
        String login_h = login,
            password_h = hasher.bCrypt(password);


        /*
        * Получаем userDAO и создаём нового пользователя
        * */
        SmallUserTransfer user = userDAO.createUser(login_h, password_h,  email);

        if(user == null)
            return null;

        /*
        * Создаём для пользователя токен и добавляем его в куки
        * */
        if(!createUserToken(user.getId(), response))
            return null;

        /*
        * Возвращаем созданного пользователя
        * */
        return user;
    }

    /**
     * check user with this
     * @param login_h = SHA-256 hash mail
     * @param password_h - SHA-256 hash password
     *                   if found then return it else return null
     * */
    @Override
    public SmallUserTransfer checkUser(String login_h, String password_h,
                                       boolean createCookie, HttpServletResponse response) {



        /*
        * Получаем userDAO и проверяем есть ли такой пользователь
        * */
        SmallUserTransfer user = userDAO.checkUser(login_h, password_h);

        if(user == null){
            return null;
        }

        /*
        * Возвращаем найденного пользователя
        * */
        return user;
    }

    /**
     * updating user with this user info
     * */
    @Override
    public void updateUser(long id, String email, String firstName, String lastName, String birthDate, String bgImage, String about, List<Long> interests){

        userDAO.updateUser(id,email, firstName, lastName, java.sql.Date.valueOf(birthDate.replace(".", "-")), bgImage, about, interests);

    }

    /**
     * updating mail
     * mail_h to LogInfo,
     * mail to UserInfo
     * */
    @Override
    public void updateLogin(String login, long id){

        String login_h = login;
//            HasherClass hasher = new HasherClass( HashType.BCRYPT );
            userDAO.updateLogin(login_h, login, id);

    }

    /**
     * updating password hash in database
     * */
    @Override
    public void updatePass(String pass, long id){

            HasherClass hasher = new HasherClass( HashType.BCRYPT );
            userDAO.updatePass(hasher.bCrypt(pass), id);

    }

    /**
     * updating user image/images in database with
     * @param id
     * */
    @Override
    public void updateUserImage(long id, List<String> mainImages){
        userDAO.updateImage(id, mainImages);
    }

    @Override
    public void updateFirstName(long id, String firstName) {
        userDAO.updateFirstName(id, firstName);
    }

    @Override
    public void updateLastName(long id, String lastName) {
        userDAO.updateLastName(id, lastName);
    }

    @Override
    public void updateAbout(long id, String about) {
        userDAO.updateAbout(id, about);
    }

    @Override
    public void updateInterests(long id, List<Long> tags) {
        userDAO.updateInterests(id, tags);
    }

    @Override
    public void updateEmail(long id, String email) {
        userDAO.updateEmail(id, email);
    }

    @Override
    public void updateBgImage(long id, String image) {
        userDAO.updateBGImage(id, image);
    }

    /**
     * Get small user by name
     * */
    @Override
    public SmallUserTransfer getUserByLogin(String name){

        return userDAO.getSmallUserByLogin(name);
    }

    @Override
    public boolean createUserToken(long userId, HttpServletResponse response){

        String token = tokenService.createToken(userId);
        if(token == null){
            return false;
        }
        cookieUtil.addCookies(new HashMap<String, String>(){{
            put("secured_token", token);
        }}, response);

        return true;
    }

    @Override
    public List<SmallUserTransfer> getSmallUsers(int start, String type, int sort) {
        if(type.equals("moder")){
            return userDAO.getModers(start, usersSmallSize, sort);
        }

        return userDAO.getSmallUsers(start, usersSmallSize, orderField(type), sort);
    }

    @Override
    public List<SmallUserTransfer> findSmallUsers(int start, String query, String type, int sort) {
        if(type.equals("moder")){
            return userDAO.findModers(start, query, usersSmallSize, sort);
        }

        return userDAO.findUsers(start, usersSmallSize, query, orderField(type), sort);
    }

    @Override
    public FullUserTransfer getFullUser(long id) {
        return userDAO.getFullUser(id);
    }

    private String orderField(String type){
        switch (type) {
            case "rating":
                return "rating";

            case "new":
                return "createDate";

            default:
                return "rating";
        }
    }

    @Override
    public List<CommonUserTransfer> getCommonUsers(int start, String type, int sort) {

        return userDAO.getCommonUsers(start, usersCommonSize, type, sort);
    }

    @Override
    public List<CommonUserTransfer> findCommonUsers(int start, String search, String type, int sort) {

        return userDAO.findCommonUsers(start, usersCommonSize, search, type, sort);
    }
}
