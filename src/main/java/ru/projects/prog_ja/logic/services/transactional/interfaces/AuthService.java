package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    UserDTO createUser(String login, String password, String email,
                       boolean createCookie, HttpServletResponse response);

    UserDTO checkUser(String login, String pass_h, boolean createCookie,
                      HttpServletResponse response);

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

    boolean updateLogin(long userId, String login);

    UserDTO getUserByLogin(String login);

}
