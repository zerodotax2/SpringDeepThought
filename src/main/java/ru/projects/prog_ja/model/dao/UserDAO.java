package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

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
    PageableEntity findUsers(int start, int size, String search, String type, int sort);

    /**
     * @return common users founded by this search string
     * */
    PageableEntity findCommonUsers(int start, int size, String search, String type, int sort);

    PageableEntity getSmallUsers(int start, int size, String type,int sort);

    PageableEntity getCommonUsers(int start, int size, String type, int sort);

    PageableEntity getModers(int start, int size, String orderField, int sort);

    PageableEntity findModers(int start,  int size, String query, String orderField, int sort);

    PageableEntity getSmallUsersByTag(int start, int size, long tagID, String query, String orderField, int sort);
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

    boolean updateUserRate(long userId, int rate);

    String getUsername(long articleId);

    List<Long> getTagsByUser(long userId);

    List<SmallTagTransfer> getUserInterests(long userId);

    long getUsersNum();
}
