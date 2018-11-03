package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.services.transactional.interfaces.NoticeService;
import ru.projects.prog_ja.model.dao.NoticeDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDAO noticeDAO;

    @Autowired
    public NoticeServiceImpl(NoticeDAO noticeDAO) {
        this.noticeDAO = noticeDAO;
    }

    @Override
    public boolean addNotice(long userId, String message, NoticeType type) {
        return noticeDAO.addNoticeToUser(userId, message, type);
    }



    @Override
    public boolean unactivateNotices(List<Long> notices) {

        return noticeDAO.unactivateNotices(notices);
    }

    @Override
    public boolean unactivateNotice(long noticeID) {

        return noticeDAO.unactivateNotice(noticeID);
    }
}
