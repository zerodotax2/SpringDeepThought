package ru.projects.prog_ja.logic.services.interfaces;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    void removeCookies(HttpServletRequest request, HttpServletResponse response);

    SmallUserTransfer checkLogPassInCookies(HttpServletRequest request);

    void addLogPassCookies(String email_h, String pass_h, HttpServletResponse response);

    SmallUserTransfer createUser(String login, String password, String email, boolean createCookie, HttpServletResponse response);

    SmallUserTransfer checkUser(String login_h, String password_h, boolean createCookie, HttpServletResponse response);

    void updateUser(long id, String email, String firstName, String lastName, String birthDate, String bgImage, String about, List<Long> interests);

    void updateLogin(String login, long id);

    void updatePass(String pass, long id);

    void updateFirstName(long id, String firstName);

    void updateLastName(long id, String lastName);

    void updateAbout(long id, String about);

    void updateInterests(long id, List<Long> tags);

    void updateEmail(long id, String email);

    void updateBgImage(long id, String image);

    void updateUserImage(long id, List<String> mainImages);

    SmallUserTransfer getUserByLogin(String login);

    boolean createUserToken(long userId, HttpServletResponse response);

    FullUserTransfer getFullUser(long id);

    List<SmallUserTransfer> getSmallUsers(int start, String type, int sort);

    List<CommonUserTransfer> getCommonUsers(int start, String type, int sort);

    List<CommonUserTransfer> findCommonUsers(int start, String search, String type, int sort);

    List<SmallUserTransfer> findSmallUsers(int start, String query, String type, int sort);
}
