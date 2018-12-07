package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.auth.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    UserDTO createUser(String login, String password, String email,
                       boolean createCookie, HttpServletResponse response);

    boolean sendActivateEmail(String login, String email);

    String resendActivateEmail(long userId);

    boolean activateAccount(String token);

    boolean existEmail(String email);

    boolean existLogin(String login);

    UserDTO checkUser(String login, String pass_h, boolean createCookie,
                      HttpServletResponse response);

    UserDTO getUserByCookies(HttpServletRequest request);

    boolean createUserToken(long userId, HttpServletResponse response);

    /**
     * check user by token
     * */
    boolean authenticateUserToken(long userId, String token);

    /**
     *
     * create token to cookie
     * */
    String createToken(long userId);

    boolean updatePass(String pass, long id);

    boolean updateEmail(long id, String email);

    boolean activateEmail(String token);

    UserDTO getUserByLogin(String login);

    boolean restore(String email);

    boolean containsRestoreToken(String token);

    boolean restorePassword(String token, String password);
}
