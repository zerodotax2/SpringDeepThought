package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.model.dao.QuestionsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QuestionReadServiceImpl implements QuestionReadService {

    @Value("${questions.small.show.size}")
    private static int smallQuestionsSize;

    @Value("${questions.answers.small.size}")
    private static int smallAnswersSize;

    @Value("${questions.common.show.size}")
    private static int commonQuestionsSize;

    @Value("{entities.max.size}")
    private static int maxEntitiesSize;

    private QuestionsDAO questionsDAO;

    public QuestionReadServiceImpl(@Autowired QuestionsDAO questionsDAO){
        this.questionsDAO = questionsDAO;
    }

    @Override
    public List<CommonQuestionTransfer> getQuestions(int start, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultQuestions(start, type, sort);
        }else{
            return findCommonQuestions(start, query, type, sort);
        }
    }

    public List<CommonQuestionTransfer> getDefaultQuestions(int start, String type, String sort){

        return questionsDAO.getQuestions(start, commonQuestionsSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<CommonQuestionTransfer> findCommonQuestions(int start, String search, String type, String sort)  {
        if(search.matches("^[\\w|\\s]+$"))
            return getDefaultQuestions(start, type, sort);

        return questionsDAO.findQuestions(start, commonQuestionsSize, search,  getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallQuestionTransfer> findSmallQuestions(int start, String search, String type, String sort)  {
        if(search.matches("^[\\w|\\s]+$"))
            return getDefaultSmallQuestions(start, type, sort);

        return questionsDAO.findSmallQuestions(start, smallQuestionsSize, search, getOrderField(type), getSort(sort));
    }

    @Override
    public FullQuestionTransfer getOneQuestion(long id) {

        return questionsDAO.getFullQuestion(id);
    }

    @Override
    public List<SmallQuestionTransfer> getSmallQuestions(int start, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultSmallQuestions(start, type, sort);
        }else{
            return findSmallQuestions(start, query, type, sort);
        }
    }

    @Override
    public List<SmallQuestionTransfer> getDefaultSmallQuestions(int start, String type, String sort){
        return questionsDAO.getSmallQuestions(start, smallQuestionsSize, getOrderField(type), getSort(sort));
    }


    @Override
    public BySomethingContainer getQuestionsByUser(int start, String size, long userId, String type, String sort) {
        int parsedSize = getSize(size);
        List<SmallQuestionTransfer> questions = questionsDAO.getSmallQuestionsByUser(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return questions != null ? new BySomethingContainer(questions.size() > parsedSize, questions) : null;
    }

    @Override
    public BySomethingContainer getQuestionsByTag(int start, String  size, long tagId, String type, String sort) {
        int parsedSize = getSize(size);
        List<SmallQuestionTransfer> questions = questionsDAO.getSmallQuestionsByTag(start, parsedSize+1,
                tagId, getOrderField(type), getSort(sort));

        return questions != null ? new BySomethingContainer(questions.size() > parsedSize, questions) : null;
    }

    @Override
    public BySomethingContainer getAnswersByUser(int start, String size, long userId, String type, String sort){

        int parsedSize = getSize(size);
        List<ViewAnswerTransfer> answers = questionsDAO.getSmallAnswersByUser(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return answers != null ? new BySomethingContainer(answers.size() > parsedSize, answers) : null;
    }

    private int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
    }
    
    private int getSort(String sort){
        return "0".equals(sort) ? 0 : 1;
    }

    private String getOrderField(String type){
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
