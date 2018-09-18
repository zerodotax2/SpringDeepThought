package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.model.entity.user.NoticeType;

import java.util.List;

public interface NoticeDAO {

    /**
     * Getting all user
     * */
    List<SmallNoticeTransfer> getAllNoticesByUser(long userId, int start, int size);

    /**
     * Getting last 5 user notices
     * */
    List<SmallNoticeTransfer> getLastNoticesByUser(long userId, int start, int size);

    /**
     * add new notice to user
     * */
    boolean addNoticeToUser(long userId, String title, String message, NoticeType type);

    void unactivateNotice(long noticeId);
}
