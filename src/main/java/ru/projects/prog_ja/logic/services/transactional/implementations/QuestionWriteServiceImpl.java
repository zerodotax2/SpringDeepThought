package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionWriteService;
import ru.projects.prog_ja.model.dao.QuestionsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class QuestionWriteServiceImpl implements QuestionWriteService {

    private final QuestionsDAO questionsDAO;
    private final XSSGuardService xssGuardService;

    @Autowired
    public QuestionWriteServiceImpl(QuestionsDAO questionsDAO,
                                    XSSGuardService xssGuardService){
        this.questionsDAO = questionsDAO;
        this.xssGuardService = xssGuardService;
    }

    @Override
    public CommonAnswerTransfer addAnswer(long articleId, String htmlContent, long userId) {

        String securedText = xssGuardService.replaceScript(htmlContent);

        return questionsDAO.addAnswer(securedText, articleId, userId);
    }

    @Override
    public boolean setRightAnswer(long answerId, long userId) {

        return questionsDAO.setRightAnswer(answerId, userId);
    }

    @Override
    public boolean deleteAnswer(long id, long userId) {

        return questionsDAO.deleteAnswer(id, userId);
    }

    @Override
    public boolean updateAnswerRate(long answerId, int rate, long userId) {

        return questionsDAO.updateAnswerRate(answerId, getRate(rate), userId);
    }

    @Override
    public FullQuestionTransfer createQuestion(String title, List<Long> tags, String content, long userId) {

        String replaced = xssGuardService.replaceScript(content);

       return questionsDAO.createQuestion(title, tags, replaced, userId);
    }

    @Override
    public boolean updateQuestion(long id, String title, String html, List<Long> tags, long userId) {

        String replaced = xssGuardService.replaceScript(html);

       return questionsDAO.updateQuestion(id,title,tags, replaced, userId);
    }

    @Override
    public boolean deleteQuestion(long id, long userId) {

        return questionsDAO.deleteQuestion(id, userId);
    }

    @Override
    public boolean updateQuestionRate(long questionId, int rate, long userId) {

        return questionsDAO.updateQuestionRate(questionId, getRate(rate), userId);
    }

    @Override
    public boolean updateAnswer(long id, String html, long userId) {

        String replaced = xssGuardService.replaceScript(html);

        return questionsDAO.updateAnswer(id , replaced, userId);
    }

    private int getRate(int rate){
        return rate > 0 ? 1 : -1;
    }
}
