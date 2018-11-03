package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.logic.services.simple.implementations.HashType;
import ru.projects.prog_ja.logic.services.simple.interfaces.CookieService;
import ru.projects.prog_ja.logic.services.simple.interfaces.HashService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.model.dao.AuthDAO;
import ru.projects.prog_ja.model.entity.user.SecuredToken;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class AuthServiceImpl implements AuthService {



    /**
     * class to hash tokens using SHA_256 algorithm
     * */
    private final HashService hashService;
    private final CookieService cookieService;

    /**
     * class to get and put user tokens to database
     * */
    private AuthDAO authDAO;

    @Autowired
    public AuthServiceImpl(AuthDAO authDAO,
                            HashService hashService,
                           CookieService cookieService){
        this.hashService = hashService;
        this.authDAO = authDAO;
        this.cookieService = cookieService;
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
    public UserDTO createUser(String login, String password, String email,
                              boolean createCookie, HttpServletResponse response) {


        String password_h = hashService.bcrypt(password);
        /*
         * Получаем userDAO и создаём нового пользователя
         * */
        UserDTO user = authDAO.createUser(login, password_h,  email);

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

       return authDAO.updateEmail(id, email);
    }

    /**
     * updating password hash in database
     * */
    @Override
    public boolean updatePass(String pass, long id){

        return authDAO.updatePass(hashService.bcrypt(pass), id);
    }

    @Override
    public boolean updateLogin(long userId, String login){


        return  authDAO.updateLogin(login, userId);

    }
    /**
     * Get small user by name
     * */
    @Override
    public UserDTO getUserByLogin(String name){

        return authDAO.getSmallUserByLogin(name);
    }
}
