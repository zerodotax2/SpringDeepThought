package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ProblemReadServiceImpl implements ProblemReadService {

    @Value("${problems.small.show.size}")
    private static int problemsSmallSize;

    @Value("${problems.common.show.size}")
    private static int problemsCommonSize;

    @Value("{entities.max.size}")
    private static int maxEntitiesSize;

    private ProblemsDAO problemsDAO;

    public ProblemReadServiceImpl(@Autowired ProblemsDAO problemsDAO){
        this.problemsDAO = problemsDAO;
    }

    @Override
    public FullProblemTransfer getFullProblem(long problemId) {
        return problemsDAO.getOneProblem(problemId);
    }

    @Override
    public List<SmallProblemTransfer> getProblems(int start, String query, String difficult, String type, String sort) {
        if(difficult != null && !difficult.equals("")){

            return getByDifficult(start, query, difficult, type, sort);
        }else if(query != null){

            return findSmallProblems(start,query, type, sort);
        }else{

            return getSmallProblems(start, type, sort);
        }
    }

    @Override
    public List<SmallProblemTransfer> getSmallProblems(int start, String type, String sort) {

        return problemsDAO.getSmallProblems(start, problemsSmallSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallProblemTransfer> getByDifficult(int start, String query, String difficult, String type, String sort) {
        if(query !=null && query.matches("^[\\w|\\s]+$"))
            return problemsDAO.findProblemsByDifficult(start, problemsSmallSize, query,
                    getDifficult(difficult), getOrderField(type), getSort(sort));

        return problemsDAO.getProblemsByDifficult(start, problemsSmallSize, getDifficult(difficult), getOrderField(type), getSort(sort));
    }

    @Override
    public BySomethingContainer getProblemsByUser(int start, String size, long userId, String type, String sort) {

        int parsedSize = getSize(size);
        List<SmallProblemTransfer> problems = problemsDAO.getSmallProblemsByUser(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return problems != null ? new BySomethingContainer(problems.size() > parsedSize, problems) : null;
    }

    @Override
    public List<SmallProblemTransfer> findSmallProblems(int start, String search, String type, String sort) {

        if(!search.matches("^[\\w|\\s]+$"))
            return getSmallProblems(start, type, sort);

        return problemsDAO.findSmallProblems(start, problemsSmallSize, search, getOrderField(type), getSort(sort));
    }

    @Override
    public FullSolutionTransfer getProblemSolution(long problemId){

        return problemsDAO.getProblemSolution(problemId);
    }


    @Override
    public BySomethingContainer getProblemsByTag(int start, String size, long tagId, String type, String sort) {

        int parsedSize = getSize(size);
        List<SmallProblemTransfer> problems = problemsDAO.getSmallProblemsByTag(start, parsedSize+1,
                tagId, getOrderField(type), getSort(sort));

        return problems != null ? new BySomethingContainer(problems.size() > parsedSize, problems) : null;
    }

    @Override
    public BySomethingContainer getProblemsSolvedByUser(int start, String  size, long userId, String type, String sort) {

        int parsedSize = getSize(size);
        List<SmallProblemTransfer> problems = problemsDAO.getUserSolvedProblems(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return problems != null ? new BySomethingContainer(problems.size() > parsedSize, problems) : null;
    }

    private int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
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
    
    private int getSort(String s){
        return"0".equals(s) ? 0 : 1;
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
