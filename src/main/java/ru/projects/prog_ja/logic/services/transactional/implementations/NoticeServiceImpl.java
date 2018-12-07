package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.transactional.interfaces.NoticeService;
import ru.projects.prog_ja.model.dao.NoticeDAO;

import java.util.Collections;
import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDAO noticeDAO;
    private final UserCounter userCounter;

    private static int boxSize;

    @Autowired
    public NoticeServiceImpl(NoticeDAO noticeDAO,
                             UserCounter userCounter) {
        this.noticeDAO = noticeDAO;
        this.userCounter = userCounter;
    }

    @Override
    public boolean addNotice(long userId, String message, NoticeType type) {

        if(noticeDAO.addNoticeToUser(userId, message, type)){

            userCounter.incrementNotices(userId, 1);
            return true;
        }
        return false;
    }


    @Override
    public boolean unactivateNotices(List<Long> notices) {

        if(notices.size() == 0)
            return true;

        return noticeDAO.unactivateNotices(notices);
    }

    @Override
    public boolean unactivateNotice(long noticeID) {

        return noticeDAO.unactivateNotice(noticeID);
    }

    @Override
    public List<SmallNoticeTransfer> getLastNotices(long userId) {

        List<SmallNoticeTransfer> notices = noticeDAO.getNoticesByUser(userId,0,boxSize);
        noticeDAO.setWatchedNotices(userId);

        return notices == null ? Collections.emptyList() : notices;
    }

    @Override
    public List<SmallNoticeTransfer> getNotices(int start, int size, long userId) {

        List<SmallNoticeTransfer> notices = noticeDAO.getNoticesByUser(userId,start,size);

        return notices == null ? Collections.emptyList() : notices;
    }

    @Value("${notices.box.size}")
    public void setBoxSize(int bs) {
        boxSize = bs;
    }
}
