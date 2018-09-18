package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.logic.services.interfaces.ProblemService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;

import java.util.List;

@Service
@Scope("prototype")
public class ProblemServiceImpl implements ProblemService {

    @Value("${problems.small.show.size}")
    public int problemsSmallSize;

    @Value("${problems.common.show.size}")
    public int problemsCommonSize;

    private ProblemsDAO problemsDAO;

    public ProblemServiceImpl(@Autowired ProblemsDAO problemsDAO){
        this.problemsDAO = problemsDAO;
    }

    @Override
    public FullProblemTransfer getFullProblem(long problemId) {
        return problemsDAO.getOneProblem(problemId);
    }

    @Override
    public List<SmallProblemTransfer> getSmallProblems(int start, String type, int sort) {
        return problemsDAO.getSmallProblems(start, problemsSmallSize, orderField(type), sort);
    }

    @Override
    public List<SmallProblemTransfer> getByDifficult(int start, String difficult, String type, int sort) {
        return problemsDAO.getProblemsByDifficult(start, problemsSmallSize, getDifficult(difficult), type, sort);
    }

    @Override
    public List<SmallProblemTransfer> getProblemsByUser(int start, long userId, String type, int sort) {
        return problemsDAO.getSmallProblemsByUser(start, problemsSmallSize, userId, orderField(type), sort);
    }

    @Override
    public List<SmallProblemTransfer> findSmallProblems(int start, String search, String type, int sort) {
        return problemsDAO.findSmallProblems(start, problemsSmallSize, search, orderField(type), sort);
    }

    @Override
    public void createProblem(long userId, String title, String content, ProblemDifficult difficult, List<Long> tags) {
        problemsDAO.createProblem(userId, title, content, tags, difficult);
    }

    @Override
    public void updateProblem(long problemId, String title, String content, ProblemDifficult problemDifficult, List<Long> tags) {
        problemsDAO.updateProblem(problemId, title, content, tags, problemDifficult);
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

    private ProblemDifficult getDifficult(String d){
        switch (d) {
            case "hell":
                return ProblemDifficult.HELL;
            case "hard":
                return ProblemDifficult.HARD;
            case "normal":
                return ProblemDifficult.NORMAL;
            default:
                return ProblemDifficult.EASY;
        }
    }
}
