package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.NoticeType;

import java.util.List;

public interface NoticeService {

    boolean addNotice(long userId, String message, NoticeType type);

    boolean unactivateNotices(List<Long> notices);

    boolean unactivateNotice(long noticeID);


}
