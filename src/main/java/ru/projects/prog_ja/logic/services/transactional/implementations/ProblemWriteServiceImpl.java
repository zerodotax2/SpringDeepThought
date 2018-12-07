package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.exceptions.AlreadyExistException;
import ru.projects.prog_ja.exceptions.RepeatVotedException;
import ru.projects.prog_ja.logic.queues.notifications.services.ProblemNoticeService;
import ru.projects.prog_ja.logic.queues.stats.services.ProblemCounter;
import ru.projects.prog_ja.logic.queues.stats.services.TagCounter;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemWriteService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ProblemWriteServiceImpl implements ProblemWriteService {

    private static int RATE_createProblem;
    private static int RATE_addComment;
    private static int RATE_decide;

    private final ProblemsDAO problemsDAO;
    private final TagCounter tagCounter;
    private final UserCounter userCounter;
    private final ProblemCounter problemCounter;
    private final ProblemNoticeService problemNoticeService;
    private final RatingService ratingService;
    private final XSSGuardService xssGuardService;

    @Autowired
    public ProblemWriteServiceImpl(ProblemsDAO problemsDAO,
                                   XSSGuardService xssGuardService,
                                   RatingService ratingService,
                                   ProblemNoticeService noticeQueue,
                                   TagCounter tagCounter,
                                   UserCounter userCounter,
                                   ProblemCounter problemCounter) {
        this.problemsDAO = problemsDAO;
        this.xssGuardService = xssGuardService;
        this.ratingService = ratingService;
        this.problemNoticeService = noticeQueue;
        this.tagCounter = tagCounter;
        this.problemCounter = problemCounter;
        this.userCounter = userCounter;
    }

    @Override
    public boolean changeRate(long problemId, int rate, long userId) throws RepeatVotedException {

        if(problemsDAO.isProblemVoted(problemId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(problemsDAO.changeRate(problemId, parsedRate, userId)){

            problemNoticeService.rateNotice(problemId, userId);
            ratingService.updateProblemOwnerRate(problemId, parsedRate, userId);
            return true;
        }

        return false;
    }

    @Override
    public CommonCommentTransfer addComment(long problemId, String comment, long userId) {

        CommonCommentTransfer commentTransfer = problemsDAO.addComment(problemId, comment, userId);
        if(commentTransfer != null){

            userCounter.incrementComments(userId, 1);
            problemNoticeService.commentNotice(problemId, userId);
            ratingService.updateProblemOwnerRate(problemId, RATE_addComment, userId);
        }

        return commentTransfer;
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {

        return problemsDAO.updateComment(commentId, comment, userId);
    }

    @Override
    public boolean removeComment(long commentId, long userId) {

        if(problemsDAO.removeComment(commentId, userId)){

            userCounter.incrementComments(userId, -1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCommentRate(long commentId, int rate, long userId) throws RepeatVotedException{

        if(problemsDAO.isProblemCommentVoted(commentId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(problemsDAO.changeCommentRate(commentId, parsedRate, userId)){

            problemNoticeService.commentRateNotice(commentId, userId);
            ratingService.updateProblemCommentOwnerRate(commentId, parsedRate, userId);
            return true;
        }

        return false;
    }


    @Override
    public boolean updateProblem(long problemId, String title, String content, String solution, String answer, String problemDifficult, List<Long> tags, long userId) {

        String replaced = xssGuardService.replaceScript(content);
        List<Long> oldTags = problemsDAO.getTagsIDByProblem(problemId);

        if(problemsDAO.updateProblem(problemId, title, replaced, solution, answer, tags, getDifficult(problemDifficult), userId)){

            for(long tagId : oldTags){
                tagCounter.incrementProblems(tagId, -1);
            }
            for(long tagID : tags){
                tagCounter.incrementProblems(tagID, 1);
            }

            return true;
        }
        return false;
    }

    @Override
    public FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, String difficult, List<Long> tags) {

        String replaced = xssGuardService.replaceScript(content);

        FullProblemTransfer fullProblemTransfer = problemsDAO.createProblem(userId, title, replaced, solution, answer,  tags, getDifficult(difficult));
        if(fullProblemTransfer != null){

            for(long tagID: tags){
                tagCounter.incrementProblems(tagID, 1);
            }
            userCounter.incrementProblemsCreated(userId, 1);

            ratingService.updateUserRate(userId, RATE_createProblem);
        }

        return fullProblemTransfer;
    }

    @Override
    public boolean sendFeedback(long problemId, String feedback, long userId) {


        if(problemsDAO.addProblemFeedback(problemId, feedback, userId)){

            problemNoticeService.feedbackNotice(problemId, userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkAnswer(long problemId, String answer, long userId) throws AlreadyExistException {

        String problemAnswer = problemsDAO.getProblemAnswer(problemId);
        if(problemsDAO.isAlreadyDecide(problemId, userId))
            throw new AlreadyExistException();

        problemCounter.incrementAttempts(problemId, 1);
        if(problemAnswer.equals(answer)) {

            problemCounter.incrementSolved(problemId, 1);
            problemsDAO.addProblemSolveUser(problemId, userId);
            ratingService.updateUserRate(userId, RATE_decide);
            return true;
        }

        return false;
    }

    private int getRate(int rate){
        return rate == 1 ? 1 : -1;
    }

    private ProblemDifficult getDifficult(String d){
        switch (d) {
            case "HELL":
                return ProblemDifficult.HELL;
            case "HARD":
                return ProblemDifficult.HARD;
            case "NORMAL":
                return ProblemDifficult.NORMAL;
            default:
                return ProblemDifficult.EASY;
        }
    }

    @Value("${problem.create}")
    public void setRATE_createProblem(int size) {
        ProblemWriteServiceImpl.RATE_createProblem = size;
    }

    @Value("${problem.comment}")
    public void setRATE_addComment(int size) {
        ProblemWriteServiceImpl.RATE_addComment = size;
    }

    @Value("${problem.decide}")
    public void setRATE_decide(int size) {
        ProblemWriteServiceImpl.RATE_decide = size;
    }
}
