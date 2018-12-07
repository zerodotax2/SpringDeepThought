package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.view.EditProblemDTO;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.logic.queues.views.ViewsQueue;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ProblemReadServiceImpl implements ProblemReadService {

    private static int problemsSmallSize;
    private static int problemsCommonSize;
    private static int maxEntitiesSize;

    private final ProblemsDAO problemsDAO;
    private final ViewsQueue viewsQueue;
    private final ValuesParser parser;

    @Autowired
    public ProblemReadServiceImpl(ProblemsDAO problemsDAO,
                                   ViewsQueue viewsQueue,
                                   ValuesParser parser){
        this.problemsDAO = problemsDAO;
        this.viewsQueue = viewsQueue;
        this.parser = parser;
    }

    @Override
    public FullProblemTransfer getFullProblem(long problemId, UserDTO userDTO) {

        FullProblemTransfer fullProblemTransfer = problemsDAO.getOneProblem(problemId);
        if(fullProblemTransfer != null && userDTO != null && fullProblemTransfer.getUser().getId() != userDTO.getId()){
            viewsQueue.addProblemView(problemId);
        }

        return fullProblemTransfer;
    }

    @Override
    public EditProblemDTO getEditProblem(long problemId) {

        return problemsDAO.getEditProblem(problemId);
    }

    @Override
    public PageableContainer getProblems(String page, String query, String difficult, String type, String sort) {
        if(difficult != null && !difficult.equals("")){

            return getByDifficult(page, query, difficult, type, sort);
        }else if(query != null){

            return findSmallProblems(page,query, type, sort);
        }else{

            return getSmallProblems(page, type, sort);
        }
    }

    @Override
    public PageableContainer getSmallProblems(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                problemsDAO.getSmallProblems((parsedPage - 1) * problemsSmallSize,
                        problemsSmallSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), problemsSmallSize));
    }

    public PageableContainer findProblemsByDifficult(String page, String query, String difficult, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                problemsDAO.findProblemsByDifficult((parsedPage - 1) * problemsSmallSize, problemsSmallSize, query,
                        parser.getDifficult(difficult), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), problemsSmallSize));
    }

    @Override
    public PageableContainer getByDifficult(String page, String query, String difficult, String type, String sort) {
        if(query !=null && RegexUtil.string(query).matches())
            findProblemsByDifficult(page, query, difficult, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                problemsDAO.getProblemsByDifficult((parsedPage - 1) * problemsSmallSize,
                        problemsSmallSize, parser.getDifficult(difficult), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), problemsSmallSize));
    }

    @Override
    public PageableContainer getProblemsByUser(String page, String size, long userId, String difficult, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = problemsDAO.getSmallProblemsByUser((parsedPage -1 ) * parsedSize, parsedSize+1,
                userId, parser.getDifficult(difficult), parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer findSmallProblems(String page, String search, String type, String sort) {

        if(search !=null && RegexUtil.string(search).matches())
            return getSmallProblems(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                problemsDAO.findSmallProblems((parsedPage-1)*problemsSmallSize,
                        problemsSmallSize, search, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), problemsSmallSize));
    }

    @Override
    public FullSolutionTransfer getProblemSolution(long problemId){

        return problemsDAO.getProblemSolution(problemId);
    }


    @Override
    public PageableContainer getProblemsByTag(String page, String size, long tagId, String difficult, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = problemsDAO.getSmallProblemsByTag((parsedPage-1)*parsedSize,
                parsedSize+1,
                tagId, parser.getDifficult(difficult), parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getProblemsSolvedByUser(String page, String  size, long userId, String difficult, String q,
                                                        String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);

        PageableEntity pageable = problemsDAO.getUserSolvedProblems((parsedPage-1)*parsedSize, parsedSize+1,
                userId, parser.getDifficult(difficult), parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }
    

    private String getOrderField(String type){

        if(type == null)
            return "rating";

        switch (type) {
            case "rating":
                return "rating";

            case "date":
                return "createDate";

            default:
                return "rating";
        }
    }

    @Value("${problems.small.show.size}")
    public void setProblemsSmallSize(int size) {
        problemsSmallSize = size;
    }

    @Value("${problems.common.show.size}")
    public void setProblemsCommonSize(int size) {
        problemsCommonSize = size;
    }

    @Value("${entities.max.size}")
    public void setMaxEntitiesSize(int size) {
        maxEntitiesSize = size;
    }
}
