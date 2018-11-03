package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallFeedbackDTO;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;
import ru.projects.prog_ja.model.entity.articles.ArticleInfo;
import ru.projects.prog_ja.model.entity.problems.*;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Scope("prototype")
public class HibernateProblemsDAO extends GenericDAO implements ProblemsDAO{

    private static final String ID_COLUMN = "problemId";
    private static final String ENTITIES_NAME = "problems";

    private final AttachTagService<ProblemsTags> attachTags;


    public HibernateProblemsDAO(@Autowired SessionFactory sessionFactory,
                                @Autowired AttachTagService<ProblemsTags> attachTags) {
        super(sessionFactory);
        this.attachTags = attachTags;
    }

    @Override
    public FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null){
                return null;
            }

            Problem problem = new Problem(title, difficult);
            problem.setCreator(user);

            Set<ProblemsTags> problemsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                problemsTags.add(new ProblemsTags(problem, tag));
            });
            if(problemsTags.size() < 3){
                return null;
            }
            problem.setTags(problemsTags);

            ProblemContent problemContent = new ProblemContent(content);
            problem.setContent(problemContent);

            ProblemSolution problemSolution = new ProblemSolution(solution, answer);
            problem.setProblemSolution(problemSolution);

            return getOneProblem((long) session.save(problem));

        }catch (Exception e){
            return null;
        }

    }

    @Override
    public boolean updateProblem(long problemId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult, long userId) {

        try {
            Session session = session();

            Problem problem = session.createNamedQuery(Problem.GET_FULL_PROBLEM, Problem.class)
                    .setParameter("id", problemId).stream().findFirst().orElse(null);
            if(problem == null || problem.getCreator().getUserId()!= userId)
                return false;

            Set<ProblemsTags> problemsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                problemsTags.add(new ProblemsTags(problem, tag));
            });
            if(problemsTags.size() < 3){
                return false;
            }
            problem.setTags(problemsTags);

            problem.setTitle(title);
            problem.setTags(problemsTags);
            problem.setDifficult(difficult);

            problem.getContent().setHtmlContent(content);

            problem.getProblemSolution().setSolution(solution);
            problem.getProblemSolution().setAnswer(answer);

            session.save(problem);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean updateTitle(long problemId, String title) {

        try {
            return session().createNamedQuery(Problem.UPDATE_PROBLEM_TITLE)
                    .setParameter("id", problemId)
                    .setParameter("title", title)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateContent(long problemId, String content) {

        try {
            return session().createNamedQuery(Problem.UPDATE_PROBLEM_CONTENT)
                    .setParameter("id", problemId)
                    .setParameter("content", content)
                    .executeUpdate() !=0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateTags(long problemId, List<Long> tags) {
        try {
            Session session = session();

            Problem problem = session.find(Problem.class, problemId);
            if(problem == null){
                return false;
            }

            Set<ProblemsTags> problemsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                problemsTags.add(new ProblemsTags(problem, tag));
            });
            if(problemsTags.size() < 3){
                return false;
            }
            problem.setTags(problemsTags);

            session.save(problem);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDifficult(long problemId, ProblemDifficult problemDifficult) {
        try {
            return session().createNamedQuery(Problem.UPDATE_PROBLEM_DIFFICULT)
                    .setParameter("difficult", problemDifficult)
                    .setParameter("id", problemId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean addProblemSolveUser(long problemId, long userId) {
        try{
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return false;


            Problem problem = session.load(Problem.class, problemId);
            if(problem == null)
                return false;

            ProblemsSolvedUsers problemsSolvedUsers = new ProblemsSolvedUsers(problem, user);
            session.save(problemsSolvedUsers);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean addProblemFeedback(long problemId, String text, long userId) {
        try {

            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return false;


            Problem problem = session.load(Problem.class, problemId);
            if(problem == null)
                return false;

            ProblemFeedback problemFeedback = new ProblemFeedback(text, new Date());
            problemFeedback.setProblem(problem);
            problemFeedback.setUserInfo(user);

            session.save(problemFeedback);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public List<SmallFeedbackDTO> getProblemFeedback(long problemId, int start, int size) {

        return session().createNamedQuery(Problem.GET_PROBLEM_FEEDBACK, SmallFeedbackDTO.class)
                .setParameter("id", problemId)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public String getProblemAnswer(long problemId) {

        return session().createNamedQuery(Problem.GET_PROBLEM_ANSWER, String.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<SmallProblemTransfer> getSmallProblems(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallProblemTransfer> getProblemsByDifficult(int start, int size, ProblemDifficult difficult, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.equal(problem.get("difficult"), difficult));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallProblemTransfer> findProblemsByDifficult(int start, int size, String search, ProblemDifficult difficult, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.and(cb.equal(problem.get("difficult"), difficult),
                cb.like(problem.get("title"), "%"+search.replace(" ", "%")+"%")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallProblemTransfer> findSmallProblems(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.like(problem.get("title"), "%"+search.replace(" ", "%")+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public FullProblemTransfer getOneProblem(long problemId) {

        FullProblemTransfer problem = session().createNamedQuery(Problem.GET_FULL_PROBLEM, FullProblemTransfer.class)
                .setParameter("id", problemId).stream().findFirst().orElse(null);
        if(problem == null){
            return null;
        }

        problem.setTags(new HashSet<>(getTagsByProblem(problemId)));

        return problem;
    }

    @Override
    public List<SmallTagTransfer> getTagsByProblem(long problemId){

       return   session().createNamedQuery(Problem.GET_TAGS_BY_PROBLEMS, SmallTagTransfer.class)
                .setParameter("id", problemId)
                .getResultList();

    }

    @Override
    public FullSolutionTransfer getProblemSolution(long problemId){

        FullSolutionTransfer fullSolutionTransfer = session().createNamedQuery(Problem.GET_PROBLEM_SOLUTION, FullSolutionTransfer.class)
                .setParameter("id", problemId)
                .stream().findFirst().orElse(null);
        if(fullSolutionTransfer == null){
            return null;
        }

        fullSolutionTransfer.setComments(new HashSet<>(getProblemComments(problemId)));

        return fullSolutionTransfer;

    }

    @Override
    public List<CommonCommentTransfer> getProblemComments(long problemId){

        return session().createNamedQuery(Problem.GET_COMMENTS_BY_PROBLEM, CommonCommentTransfer.class)
                .setParameter("id", problemId)
                .getResultList();

    }

    @Override
    public boolean updateSolution(long problemId, String solution) {
        try {
            return session().createNamedQuery(Problem.UPDATE_PROBLEM_SOLUTION)
                    .setParameter("solution", solution)
                    .setParameter("id", problemId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateAnswer(long problemId, String answer) {
        try {
            return session().createNamedQuery(Problem.UPDATE_PROBLEM_SOLUTION)
                    .setParameter("answer", answer)
                    .setParameter("id", problemId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteProblem(long problemId, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(Problem.DELETE_PROBLEM)
                    .setParameter("id", problemId)
                    .setParameter("user", session.load(UserInfo.class,userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getProblemsNum() {
        return (long) session().createNamedQuery(Problem.COUNT_PROBLEMS, Object.class)
                .getSingleResult();
    }

    @Override
    public List<SmallProblemTransfer> getSmallProblemsByTag(int start, int size, long tagID, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemsTags> tags = problem.join("tags");

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.equal(tags.get("tagId"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallProblemTransfer> getSmallProblemsByUser(int start, int size, long userId, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, UserInfo> creator = problem.join("creator");

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.equal(creator.get("userId"), userId));
        
        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallProblemTransfer> getUserSolvedProblems(int start, int size, long userId, String type, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemsSolvedUsers> solvers = problem.join("solvers");

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.equal(solvers.get("user"), session.load(UserInfo.class, userId)));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(type)));
        }else{
            query.orderBy(cb.desc(problem.get(type)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }


    @Override
    public boolean changeCommentRate(long commentId, int rate, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(Problem.UPDATE_PROBLEM_RATE)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean changeRate(long commentId, int rate, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(Problem.UPDATE_PROBLEM_COMMENT_RATE)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public CommonCommentTransfer addComment(long problemId, String comment, long userId) {
        try {
            Session session = session();

            Problem problem = session.load(Problem.class, problemId);
            if(problem == null){
                return null;
            }

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null){
                return null;
            }

            ProblemComment problemComment = new ProblemComment(comment);
            problemComment.setProblem(problem);
            problemComment.setCreator(user);

            return getProblemComment((long) session.save(problem));
        }catch (Exception e){
            return null;
        }
    }

    public CommonCommentTransfer getProblemComment(long problemId){

        return session().createNamedQuery(Problem.GET_COMMENT, CommonCommentTransfer.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(Problem.UPDATE_PROBLEM_COMMENT)
                    .setParameter("comment", comment)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .setParameter("id", commentId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeComment(long commentId, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(Problem.REMOVE_PROBLEM_COMMENT)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .setParameter("id", commentId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }
}
