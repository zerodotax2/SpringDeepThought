package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.logic.services.interfaces.QuestionService;
import ru.projects.prog_ja.model.dao.QuestionsDAO;
import ru.projects.prog_ja.model.entity.questions.Questions;

import java.util.List;

@Service
@Scope("prototype")
public class QuestionServiceImpl implements QuestionService {

    @Value("${questions.small.show.size}")
    private int smallQuestionsSize;

    @Value("${questions.common.show.size}")
    private int commonQuestionsSize;

    private QuestionsDAO questionsDAO;

    public QuestionServiceImpl(@Autowired QuestionsDAO questionsDAO){
        this.questionsDAO = questionsDAO;
    }

    @Override
    public List<CommonQuestionTransfer> getQuestions(int start, String type, int sort) {
        return questionsDAO.getQuestions(start, commonQuestionsSize, orderField(type), sort);
    }

    @Override
    public List<CommonQuestionTransfer> findCommonQuestions(int start, String search, String type, int sort) {
        return questionsDAO.findQuestions(start, commonQuestionsSize, search,  orderField(type), sort);
    }

    @Override
    public List<SmallQuestionTransfer> findSmallQuestions(int start, String search, String type, int sort) {
        return questionsDAO.findSmallQuestions(start, smallQuestionsSize, search, orderField(type), sort);
    }

    @Override
    public List<SmallQuestionTransfer> getSmallQuestions(int start, String type, int sort) {
        return questionsDAO.getSmallQuestions(start, smallQuestionsSize, orderField(type), sort);
    }

    @Override
    public CommonAnswerTransfer addAnswer(long articleId, String htmlContent, long userId) {

        long id = questionsDAO.addAnswer(htmlContent, articleId, userId);
        if(id == 0){
            return null;
        }
        return questionsDAO.getAnswer(id);
    }

    @Override
    public void createQuestion(String title, List<Long> tags, String content, long userId) {
        questionsDAO.createQuestion(title, tags, content, userId);
    }

    @Override
    public void updateQuestion(long id, String title, String html, List<Long> tags) {
        questionsDAO.updateQuestion(id,title,tags,html);
    }

    @Override
    public FullQuestionTransfer getOneQuestion(long id) {
        return questionsDAO.getFullQuestion(id);
    }

    @Override
    public void updateAnswer(long id, String html) {
        questionsDAO.updateAnswer(id ,html);
    }

    @Override
    public List<SmallQuestionTransfer> getQuestionsByUser(int start, long userId, String type, int sort) {
        return questionsDAO.getSmallQuestionsByUser(start, smallQuestionsSize, userId, orderField(type), sort);
    }

    private String orderField(String type){
        switch (type) {
            case "rating":
                return "rating";

            case "date":
                return "createDate";

            default:
                return "rating";
        }
    }
}
