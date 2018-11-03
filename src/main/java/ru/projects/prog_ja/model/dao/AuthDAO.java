package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.entity.user.SecuredToken;

public interface AuthDAO {

    /**
     * save random token to user
     * */
    boolean saveToken(long userId, String salt, String securedToken);

    /**
     * get token by user
     * */
    SecuredToken getTokenByUser(long userId);

    boolean updateEmail(long id, String email);

    UserDTO checkUser(String login, String password);

    UserDTO getSmallUserByLogin(String login);

    /**
     * User create new password then we hash and save it
     *
     * if user not found then do nothing
     **/
    boolean updatePass(String pass_h, long id);

    /**
     * User create new mail then we hash and save it
     * if user not found then do nothing
     **/
    boolean updateLogin(String login, long id);


    /**
     *
     * create user with this params
     * @return created user
     */
    UserDTO createUser(String login_h, String password_h, String email);
}
