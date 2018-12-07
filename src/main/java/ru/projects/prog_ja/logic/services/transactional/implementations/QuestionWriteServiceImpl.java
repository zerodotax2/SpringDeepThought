package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.exceptions.RepeatVotedException;
import ru.projects.prog_ja.logic.queues.notifications.services.QuestionNoticeService;
import ru.projects.prog_ja.logic.queues.stats.services.TagCounter;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionWriteService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;
import ru.projects.prog_ja.model.dao.QuestionsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class QuestionWriteServiceImpl implements QuestionWriteService {

    private static int RATE_questionCreate;
    private static int RATE_addAnswer;
    private static int RATE_rightAnswer;

    private final QuestionsDAO questionsDAO;
    private final QuestionNoticeService questionNoticeService;
    private final RatingService ratingService;
    private final XSSGuardService xssGuardService;
    private final TagCounter tagCounter;
    private final UserCounter userCounter;

    @Autowired
    public QuestionWriteServiceImpl(QuestionsDAO questionsDAO,
                                    XSSGuardService xssGuardService,
                                    RatingService ratingService,
                                    QuestionNoticeService questionNoticeService,
                                    TagCounter tagCounter,
                                    UserCounter userCounter){
        this.questionsDAO = questionsDAO;
        this.xssGuardService = xssGuardService;
        this.ratingService = ratingService;
        this.questionNoticeService = questionNoticeService;
        this.tagCounter = tagCounter;
        this.userCounter = userCounter;
    }

    @Override
    public CommonAnswerTransfer addAnswer(long questionId, String htmlContent, long userId) {

        String securedText = xssGuardService.replaceScript(htmlContent);

        CommonAnswerTransfer answer = questionsDAO.addAnswer(securedText, questionId, userId);
        if(answer != null){

            userCounter.incrementAnswers(userId, 1);
            questionNoticeService.answerNotice(questionId, userId);
            ratingService.updateQuestionOwnerRate(questionId, RATE_addAnswer, userId);
        }

        return answer;
    }

    @Override
    public boolean setRightAnswer(long answerId, long userId) {

        if(questionsDAO.setRightAnswer(answerId, userId)){

            questionNoticeService.rightAnswerNotice(answerId);
            ratingService.updateAnswerOwnerRate(answerId, RATE_rightAnswer, userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAnswer(long id, long userId) {

        if(questionsDAO.deleteAnswer(id, userId)){

            userCounter.incrementAnswers(id, -1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAnswerRate(long answerId, int rate, long userId) throws RepeatVotedException {

        if(questionsDAO.isAnswerVoted(answerId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(questionsDAO.updateAnswerRate(answerId, parsedRate, userId)){

            questionNoticeService.answerRateNotice(answerId, userId);
            ratingService.updateAnswerOwnerRate(answerId, parsedRate, userId);
            return true;
        }

        return false;
    }

    @Override
    public FullQuestionTransfer createQuestion(String title, List<Long> tags, String content, long userId) {

        String replaced = xssGuardService.replaceScript(content);

        FullQuestionTransfer questionTransfer = questionsDAO.createQuestion(title, tags, replaced, userId);
        if(questionTransfer != null){

            for(long tagID : tags){
                tagCounter.incrementQuestions(tagID, 1);
            }
            userCounter.incrementProblemsCreated(userId, 1);

            ratingService.updateUserRate(userId, RATE_questionCreate);
        }

        return questionTransfer ;
    }

    @Override
    public boolean updateQuestion(long id, String title, String html, List<Long> tags, long userId) {

        String replaced = xssGuardService.replaceScript(html);
        List<Long> oldTags = questionsDAO.getTagsByQuestion(id);

       if(questionsDAO.updateQuestion(id,title,tags, replaced, userId)){

           for(long tagId : oldTags){
               tagCounter.incrementQuestions(tagId, -1);
           }
           for(long tagId : tags){
                tagCounter.incrementQuestions(tagId, 1);
           }
           return true;
       }
       return false;
    }

    @Override
    public boolean deleteQuestion(long id, long userId) {

        if(questionsDAO.deleteQuestion(id, userId)){

            userCounter.incrementQuestions(userId, -1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuestionRate(long questionId, int rate, long userId) throws RepeatVotedException{

        if(questionsDAO.isQuestionVoted(questionId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(questionsDAO.updateQuestionRate(questionId, parsedRate, userId)){

            questionNoticeService.rateNotice(questionId, userId);
            ratingService.updateQuestionOwnerRate(questionId, parsedRate, userId);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateAnswer(long id, String html, long userId) {

        String replaced = xssGuardService.replaceScript(html);

        return questionsDAO.updateAnswer(id , replaced, userId);
    }

    private int getRate(int rate){
        return rate > 0 ? 1 : -1;
    }

    @Value("${question.create}")
    public  void setRATE_questionCreate(int rate) {
        RATE_questionCreate = rate;
    }

    @Value("${question.answer}")
    public  void setRATE_addAnswer(int rate) {
        RATE_addAnswer = rate;
    }

    @Value("${question.answer.right}")
    public  void setRATE_rightAnswer(int rate) {
        RATE_rightAnswer = rate;
    }
}
