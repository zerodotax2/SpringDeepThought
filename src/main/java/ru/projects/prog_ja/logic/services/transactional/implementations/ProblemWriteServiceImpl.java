package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemWriteService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ProblemWriteServiceImpl implements ProblemWriteService {

    private final ProblemsDAO problemsDAO;
    private final XSSGuardService xssGuardService;

    @Autowired
    public ProblemWriteServiceImpl(ProblemsDAO problemsDAO,
                                   XSSGuardService xssGuardService) {
        this.problemsDAO = problemsDAO;
        this.xssGuardService = xssGuardService;
    }

    @Override
    public boolean changeRate(long problemId, int rate, long userId) {

        return problemsDAO.changeRate(problemId, rate, userId);
    }

    @Override
    public CommonCommentTransfer addComment(long problemId, String comment, long userId) {

        return problemsDAO.addComment(problemId, comment, userId);
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {

        return problemsDAO.updateComment(commentId, comment, userId);
    }

    @Override
    public boolean removeComment(long commentId, long userId) {

        return problemsDAO.removeComment(commentId, userId);
    }

    @Override
    public boolean updateCommentRate(long commentId, int rate, long userId) {

        return problemsDAO.changeCommentRate(commentId, rate, userId);
    }


    @Override
    public boolean updateProblem(long problemId, String title, String content, String solution, String answer, String problemDifficult, List<Long> tags, long userId) {

        String replaced = xssGuardService.replaceScript(content);

        return problemsDAO.updateProblem(problemId, title, replaced, solution, answer, tags, getDifficult(problemDifficult), userId);
    }

    @Override
    public FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, String difficult, List<Long> tags) {

        String replaced = xssGuardService.replaceScript(content);

        return problemsDAO.createProblem(userId, title, replaced, solution, answer,  tags, getDifficult(difficult));
    }

    @Override
    public boolean sendFeedback(long problemId, String feedback, long userId) {

        return problemsDAO.addProblemFeedback(problemId, feedback, userId);
    }

    @Override
    public boolean checkAnswer(long problemId, String answer, long userId) {

        String problemAnswer = problemsDAO.getProblemAnswer(problemId);
        if(problemAnswer.equals(answer)) {
            problemsDAO.addProblemSolveUser(problemId, userId);
            return true;
        }

        return false;
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
}
