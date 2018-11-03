package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.full.FullForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.SupportService;
import ru.projects.prog_ja.model.dao.SupportDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class SupportServiceImpl implements SupportService {


    private final SupportDAO supportDAO;

    @Autowired
    public SupportServiceImpl(SupportDAO supportDAO) {
        this.supportDAO = supportDAO;
    }

    @Override
    public FullForumAnswer ask(String text, long userId) {

        return supportDAO.addForumQuestion(text, userId);
    }

    @Override
    public List<SmallUserQuestionTransfer> getUserQuestions(int start, int size) {

        return supportDAO.getForumQuestions(start, size);
    }

    @Override
    public List<SmallUserQuestionTransfer> getNonAnsweredQuestions(int start, int size) {

        return supportDAO.getNonAnsweredForumQuestions(start, size);
    }

    @Override
    public boolean answer(long id, String text, long userId) {

        return supportDAO.answerForumQuestion(id, text, userId);
    }

    @Override
    public FullForumAnswer getFullAnswer(long id) {

        return supportDAO.getOneQuestion(id);
    }
}
