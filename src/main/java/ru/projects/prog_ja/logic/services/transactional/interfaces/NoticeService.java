package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;

import java.util.List;

public interface NoticeService {

    boolean addNotice(long userId, String message, NoticeType type);

    boolean unactivateNotices(List<Long> notices);

    boolean unactivateNotice(long noticeID);

    List<SmallNoticeTransfer> getLastNotices(long userId);

    List<SmallNoticeTransfer> getNotices(int start, int size, long userId);
}
