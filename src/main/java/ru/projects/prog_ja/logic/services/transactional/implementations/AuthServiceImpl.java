package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.auth.AuthDTO;
import ru.projects.prog_ja.dto.auth.RestoreMessage;
import ru.projects.prog_ja.dto.auth.UpdateEmail;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.logic.caches.interfaces.AuthCache;
import ru.projects.prog_ja.logic.queues.notifications.services.ForumNoticeService;
import ru.projects.prog_ja.logic.services.simple.implementations.HashType;
import ru.projects.prog_ja.logic.services.simple.interfaces.CookieService;
import ru.projects.prog_ja.logic.services.simple.interfaces.HashService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.EmailService;
import ru.projects.prog_ja.model.dao.AuthDAO;
import ru.projects.prog_ja.model.entity.user.SecuredToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class AuthServiceImpl implements AuthService {



    /**
     * class to hash tokens using SHA_256 algorithm
     * */
    private final HashService hashService;
    private final CookieService cookieService;
    private final ForumNoticeService noticeService;
    private final AuthCache authCache;
    private final EmailService emailService;

    /**
     * class to get and put user tokens to database
     * */
    private AuthDAO authDAO;

    @Autowired
    public AuthServiceImpl(AuthDAO authDAO,
                            HashService hashService,
                           CookieService cookieService,
                           ForumNoticeService noticeService,
                           AuthCache authCache,
                           EmailService emailService){
        this.hashService = hashService;
        this.authDAO = authDAO;
        this.cookieService = cookieService;
        this.noticeService = noticeService;
        this.authCache = authCache;
        this.emailService = emailService;
    }

    /**
     * create new user with this params
     * @param email - create SHA-256 hash for LogInfo and add in UserInfo
     * @param password create bcrypt hash for LogInfo
     * @param login - login for UserInfo
     * */
    @Override
    public UserDTO createUser(String login, String password, String email,
                              boolean createCookie, HttpServletResponse response){


        String password_h = hashService.bcrypt(password);
        /*
         * Получаем userDAO и создаём нового пользователя
         * */
        UserDTO user = authDAO.createUser(login, password_h,  email);

        if(user == null)
            return null;

        if(!sendActivateEmail(login, email))
            return null;

        noticeService.accountCreateNotice(user.getId());
        /*
         * Возвращаем созданного пользователя
         * */
        return user;
    }

    @Override
    public String resendActivateEmail(long userId) {

        AuthDTO auth = authDAO.getAuthDTOByUserId(userId);
        if(auth == null) return null;

        if(!sendActivateEmail(auth.getLogin(), auth.getEmail()))
            return null;

        return auth.getEmail();
    }

    @Override
    public boolean sendActivateEmail(String login, String email){

        String token = hashService.getSalt(64);

        if(!authDAO.saveActivateToken(email, token))
            return false;

        return emailService.sendActivateEmail(login, email, token);
    }

    @Override
    public boolean activateAccount(String token) {

        return authDAO.activateAccount(token);
    }

    /**
     * check user with this
     * @param password_h - SHA-256 hash password
     *                   if found then return it else return null
     * */
    @Override
    public UserDTO checkUser(String login, String password_h,
                             boolean createCookie, HttpServletResponse response) {



        /*
         * Получаем userDAO и проверяем есть ли такой пользователь
         * */
        UserDTO user = authDAO.checkUser(login, password_h);

        if(user == null){
            return null;
        }

        /*
         * Возвращаем найденного пользователя
         * */
        return user;
    }

    @Override
    public boolean existEmail(String email) {
        return !authDAO.isFreeEmail(email);
    }

    @Override
    public boolean existLogin(String login) {
        return !authDAO.isFreeLogin(login);
    }

    /**
     * checking if mail_h and pass_h in cookies exist
     * if yes then check user with this mail_g and pass_h
     * and return founded
     * */
    @Override
    public UserDTO getUserByCookies(HttpServletRequest request) {

        Map<String, String> cookieMap = cookieService.findCookies(request);

        if(!cookieMap.containsKey("login") || !cookieMap.containsKey("password_h"))
            return null;

        return checkUser(cookieMap.get("login"),
                cookieMap.get("password_h"), false, null);
    }

    /**
     * достаём для данного пользователя токен из базы,
     * соединяем его с солью пользователя и проверям на валидность
     * */
    @Override
    public boolean authenticateUserToken(long userId, String token) {

        SecuredToken securedToken = authDAO.getTokenByUser(userId);
        if(securedToken == null){
            return false;
        }

        String check = hashService.hash(token + securedToken.getSalt(), HashType.SHA_256);
        if(!check.equals(securedToken.getToken())){
            return false;
        }

        return true;
    }

    /**
     * метод для создания токена пользователя,
     * получаем рандомную соль, получаем рандомный токен,
     * хешируем их вместе и получаем токен для сохранения,
     * теперь, чтобы проверить пользователя надо иметь соль и токен из базы,
     * а также токен пользователя
     * */
    @Override
    public String createToken(long userId) {

        String salt = hashService.getSalt(256);
        String randomToken = hashService.getSalt(256);

        String securedToken = hashService.hash(randomToken + salt, HashType.SHA_256);
        if(!authDAO.saveToken(userId, salt, securedToken)){
            return null;
        }

        return randomToken;
    }

    @Override
    public boolean createUserToken(long userId, HttpServletResponse response){
        String randomToken = createToken(userId);
        if(randomToken == null){
            return false;
        }

        cookieService.addCookies(new HashMap<String, String>(){{
            put("secured_token", randomToken);
        }}, response);

        return true;
    }

    @Override
    public boolean updateEmail(long id, String email) {

        long userId = authDAO.getUserIdByEmail(email);
        if(userId != -1)
            return false;

        String token = hashService.getSalt(64);

        if(!authCache.putUpdateEmailMessage(new UpdateEmail(id, email, token)))
            return false;

        return emailService.sendConfirmEmail(email, token);
    }

    @Override
    public boolean activateEmail(String token){

        UpdateEmail updateEmail = authCache.pollUpdateEmailMessage(token);
        if(updateEmail == null)
            return false;


        return authDAO.updateEmail(updateEmail.getUserId(), updateEmail.getEmail());
    }

    /**
     * updating password hash in database
     * */
    @Override
    public boolean updatePass(String pass, long id){

        return authDAO.updatePass(hashService.bcrypt(pass), id);
    }
    /**
     * Get small user by name
     * */
    @Override
    public UserDTO getUserByLogin(String name){

        return authDAO.getSmallUserByLogin(name);
    }

    @Override
    public boolean restore(String email) {

        long userId = authDAO.getUserIdByEmail(email);
        if(userId == -1)
            return false;

        String token = hashService.getSalt(64);

        if(authCache.putRestoreMessage(new RestoreMessage(userId, token)))
            return false;

        return emailService.sendRestoreEmail(token, email);
    }

    @Override
    public boolean containsRestoreToken(String token){

        return authCache.containsRestoreToken(token);
    }

    @Override
    public boolean restorePassword(String token, String password){

        RestoreMessage restoreMessage =
                authCache.pollRestoreMessage(token);
        if(restoreMessage == null)
            return false;

        String hashPassword = hashService.bcrypt(password);

        return authDAO.updatePass(hashPassword, restoreMessage.getUserId());
    }
}
