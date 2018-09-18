package ru.projects.prog_ja.logic.services.interfaces;

/**
 * Class to manage user tokens
 * */
public interface TokenService {

    /**
     * check user by token
     * */
    boolean authenticateUserToken(long userId, String token);

    /**
     *
     * create token to cookie
     * */
    String createToken(long userId);
}
