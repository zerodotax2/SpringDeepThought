package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.UserDTO;
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
     * do full update user with this parameters
     * */
    boolean updateUser(long id, String firstName, String lastName, Date birthDate, String bgImage, String about, List<Long> tags);
    /**
     * checkIf user exist in database
     * @return founded user
     */


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

    List<SmallUserTransfer> getModers(int start, int size, String orderField, int sort);

    List<SmallUserTransfer> findModers(int start,  int size, String query, String orderField, int sort);

    List<SmallUserTransfer> getSmallUsersByTag(int start, int size, long tagID, String orderField, int sort);
    /**
     * delete user with
     * @param  id
     * */
    boolean deleteUser(long id);

    /**
     * update image of user with
     * @param id
     * and using
     * @param mainImages
     * */
    boolean updateImage(long id, List<String> mainImages);

    /**
     * update background profile image
     * by user with next id
     * @param id
     * */
    boolean updateBGImage(long id, String imagePath);

    boolean updateFirstName(long id, String firstName);

    boolean updateLastName(long id, String lastName);

    boolean updateAbout(long id, String about);

    boolean updateInterests(long id, List<Long> tags);

    boolean updateBirthdate(long id, Date date);

    long getUsersNum();
}
