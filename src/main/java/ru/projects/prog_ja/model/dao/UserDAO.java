package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.entity.user.SecuredToken;

import java.sql.Date;
import java.util.List;

/**
 * interface that contains common methods to work with users
*/
public interface UserDAO {

    /**
     *
     * create user with this params
     * @return created user
     */
    SmallUserTransfer createUser(String login_h, String password_h, String email);

    /**
     * do full update user with this parameters
     * */
    void updateUser(long id, String email, String firstName, String lastName, Date birthDate, String bgImage, String about, List<Long> tags);
    /**
     * checkIf user exist in database
     * @return founded user
     */
    SmallUserTransfer checkUser(String login, String password);


    SmallUserTransfer getSmallUserByLogin(String login);

    /**
     * @return common user profile that can see every user
     * */
    CommonUserTransfer getCommonUser(long id);

    /**
     * @return full user profile which need to change it by himself
     * */
    FullUserTransfer getFullUser(long id);

    /**
     * @return small users founded by this search string
     * */
    List<SmallUserTransfer> findUsers(int start, int size, String search, String type, int sort);

    /**
     * @return common users founded by this search string
     * */
    List<CommonUserTransfer> findCommonUsers(int start, int size, String search, String type, int sort);

    List<SmallUserTransfer> getSmallUsers(int start, int size, String type,int sort);

    List<CommonUserTransfer> getCommonUsers(int start, int size, String type, int sort);

    List<SmallUserTransfer> getModers(int start, int size, int sort);

    List<SmallUserTransfer> findModers(int start, String query, int size, int sort);

    List<SmallUserTransfer> getSmallUsersByTag(int start, int size, long tagID, String orderField, int sort);
    /**
     * delete user with
     * @param  id
     * */
    void deleteUser(long id);

    /**
     * update image of user with
     * @param id
     * and using
     * @param mainImages
     * */
    void updateImage(long id, List<String> mainImages);

    /**
     * update background profile image
     * by user with next id
     * @param id
     * */
    void updateBGImage(long id, String imagePath);

    /**
     * User create new password then we hash and save it
     *
     * if user not found then do nothing
     **/
    void updatePass(String pass_h, long id);

    /**
     * User create new mail then we hash and save it
     * if user not found then do nothing
     **/
    void updateLogin(String login_h, String login, long id);

    void updateFirstName(long id, String firstName);

    void updateLastName(long id, String lastName);

    void updateAbout(long id, String about);

    void updateInterests(long id, List<Long> tags);

    void updateEmail(long id, String email);

    /**
     * save random token to user
     * */
    boolean saveToken(long userId, String salt, String securedToken);

    /**
     * get token by user
     * */
    SecuredToken getTokenByUser(long userId);

    long getUsersNum();
}
