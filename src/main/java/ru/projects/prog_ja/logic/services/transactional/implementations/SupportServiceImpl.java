package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.full.FullForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;
import ru.projects.prog_ja.logic.queues.notifications.services.ForumNoticeService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.SupportService;
import ru.projects.prog_ja.model.dao.SupportDAO;

import java.util.Collections;
import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class SupportServiceImpl implements SupportService {


    private final SupportDAO supportDAO;
    private final ForumNoticeService forumNoticeService;

    @Autowired
    public SupportServiceImpl(SupportDAO supportDAO,
                              ForumNoticeService forumNoticeService) {
        this.supportDAO = supportDAO;
        this.forumNoticeService = forumNoticeService;
    }

    @Override
    public FullForumAnswer ask(String text, long userId) {

        return supportDAO.addForumQuestion(text, userId);
    }

    @Override
    public List<SmallUserQuestionTransfer> getUserQuestions(int start, int size) {

        List<SmallUserQuestionTransfer> questions
                = supportDAO.getForumQuestions(start, size);

        return questions == null ? Collections.emptyList() : questions;
    }

    @Override
    public List<SmallUserQuestionTransfer> getNonAnsweredQuestions(int start, int size) {

        List<SmallUserQuestionTransfer> questions =
                supportDAO.getNonAnsweredForumQuestions(start, size);
        return questions == null ? Collections.emptyList() : questions;
    }

    @Override
    public boolean answer(long id, String text, long userId) {

        if(supportDAO.answerForumQuestion(id, text, userId)){

            forumNoticeService.answerForumQuestion(id, userId);
            return true;
        }
        return false;
    }

    @Override
    public FullForumAnswer getFullAnswer(long id) {

        return supportDAO.getOneQuestion(id);
    }
}
