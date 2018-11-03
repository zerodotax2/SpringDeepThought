package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;

import java.sql.Date;
import java.util.List;

/**
 * Dao to work with user birthdates
 * */
public interface BirthdatesDAO {

    /**
     * @return users which have birthday today
     * */
    List<CommonUserTransfer> getTodayUsers();

    /**
     * @return emails of users which have birthday today
     * */
    List<String> getTodayEmails();

    /**
     * @return users which have birthday in
     * @param date
     * */
    List<CommonUserTransfer> getDateUsers(Date date);
    /**
     * @return emails of users which have birthday in
     * @param date
     * */
    List<String> getDateEmails(Date date);

    /**
     * update birthdate
     * */

    boolean updateBirthdate(long userId, Date date);
}
