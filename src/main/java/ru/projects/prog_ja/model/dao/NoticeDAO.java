package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;

import java.util.List;

public interface NoticeDAO {


    /**
     * Getting last 5 user notices
     * */
    List<SmallNoticeTransfer> getLastNoticesByUser(long userId, int start, int size);


    List<SmallNoticeTransfer> getNoticesByUser(long userId, int start, int size);

    /**
     * add new notice to user
     * */
    boolean addNoticeToUser(long userId,  String message, NoticeType type);

    boolean unactivateNotice(long noticeId);

    boolean unactivateNotices(List<Long> notices);

    boolean setWatchedNotices(long userId);
}
