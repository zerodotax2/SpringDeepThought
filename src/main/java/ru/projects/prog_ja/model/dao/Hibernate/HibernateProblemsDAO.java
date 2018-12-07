package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallFeedbackDTO;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.EditProblemDTO;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.queries.ProblemQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.dao.ProblemsDAO;
import ru.projects.prog_ja.model.entity.problems.*;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@Scope("prototype")
public class HibernateProblemsDAO extends GenericDAO implements ProblemsDAO{

    private static final String ID_COLUMN = "problemId";
    private static final String ENTITIES_NAME = "problems";

    private final AttachTagService<ProblemsTags, Problem> attachTags;


    @Autowired
    public HibernateProblemsDAO(SessionFactory sessionFactory,
                                AttachTagService<ProblemsTags, Problem> attachTags) {
        super(sessionFactory);
        this.attachTags = attachTags;
    }

    @Override
    public FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            Problem problem = new Problem(title, difficult);
            problem.setCreator(user);

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            if(tagsList == null || tagsList.size() < 3){
                return null;
            }
            Set<ProblemsTags> tagsSet = new HashSet<>(tagsList.size());
            tagsList.forEach(tag -> tagsSet.add(new ProblemsTags(problem, tag)));
            problem.setTags(tagsSet);

            ProblemContent problemContent = new ProblemContent(content);
            problemContent.setProblem(problem);
            problem.setContent(problemContent);

            ProblemSolution problemSolution = new ProblemSolution(solution, answer);
            problemSolution.setProblem(problem);
            problem.setProblemSolution(problemSolution);

            ProblemCounter problemCounter = new ProblemCounter();
            problemCounter.setProblem(problem);
            problem.setCounter(problemCounter);

            long id = (long) session.save(problem);
            return getOneProblem(id);

        }catch (Exception e){
            return null;
        }

    }

    @Override
    public boolean updateProblem(long problemId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult, long userId) {

        try {
            Session session = session();

            Problem problem = session.createNamedQuery(ProblemQueries.GET_UPDATE_PROBLEM_ENTITY, Problem.class)
                    .setParameter("id", problemId).stream().findFirst().orElse(null);
            if(problem == null || problem.getCreator().getUserId()!= userId)
                return false;

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            problem.setTags(updateTags(problem, tagsList));

            problem.setTitle(title);
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

    private Set<ProblemsTags> updateTags(Problem problem, List<Tags> newTags){

        Set<ProblemsTags> oldTags = problem.getTags();

        if(newTags == null || newTags.size() < 3){
            return oldTags;
        }

        /*
         * Перебираем по новым тегам, если есть совпадение удаляем из старых и новых тегов
         * */
        for(Iterator<Tags> i = newTags.iterator(); i.hasNext();){

            Tags t = i.next();

            for(Iterator<ProblemsTags> j = oldTags.iterator(); j.hasNext();){
                ProblemsTags old = j.next();
                if(old.getTagId().getTagId() == t.getTagId()){
                    j.remove();
                    i.remove();
                    break;
                }
            }
        }
        /*
         * Всё что осталось в старых тегах удаляем
         * */
        List<Tags> tagsToDelete = new ArrayList<>(oldTags.size());
        for(ProblemsTags tag: oldTags)
            tagsToDelete.add(tag.getTagId());
        if(oldTags.size() > 0)
            session().createNamedQuery(ProblemQueries.REMOVE_PROBLEM_TAGS)
                .setParameterList("tags", tagsToDelete).executeUpdate();

        /*
         * Всё что осталось в новых тегах добавляем
         * */
        Set<ProblemsTags> tagsToAdd = new HashSet<>(newTags.size());
        for(int i = 0; i < newTags.size(); i++)
            tagsToAdd.add(new ProblemsTags(problem, newTags.get(i)));

        return tagsToAdd;
    }

    @Override
    public boolean updateTitle(long problemId, String title) {

        try {
            return session().createNamedQuery(ProblemQueries.UPDATE_PROBLEM_TITLE)
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
            Session session = session();

            Problem problem = session.load(Problem.class, problemId);

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_CONTENT)
                    .setParameter("problem", problem)
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

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            if(tagsList == null || tagsList.size() < 3){
                return false;
            }
            Set<ProblemsTags> tagsSet = new HashSet<>(tagsList.size());
            tagsList.forEach(tag -> tagsSet.add(new ProblemsTags(problem, tag)));
            problem.setTags(tagsSet);

            session.save(problem);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDifficult(long problemId, ProblemDifficult problemDifficult) {
        try {
            return session().createNamedQuery(ProblemQueries.UPDATE_PROBLEM_DIFFICULT)
                    .setParameter("difficult", problemDifficult)
                    .setParameter("id", problemId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getTitle(long problemId) {
        return session().createNamedQuery(ProblemQueries.GET_TITLE, String.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeCommentTemplateDTO getNoticeCommentTemplate(long commentId) {

        return session().createNamedQuery(ProblemQueries.GET_NOTICE_COMMENT_TEMPLATE, NoticeCommentTemplateDTO.class)
                .setParameter("id", commentId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeEntityTemplateDTO getNoticeTemplate(long problemId) {

        return session().createNamedQuery(ProblemQueries.GET_NOTICE_TEMPLATE, NoticeEntityTemplateDTO.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Long> getTagsIDByProblem(long problemId) {
        return session().createNamedQuery(ProblemQueries.GET_TAGS_IDS_BY_PROBLEM, Long.class)
                .setParameter("id", problemId).getResultList();
    }

    @Override
    public boolean addProblemSolveUser(long problemId, long userId) {
        try{
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            Problem problem = session.load(Problem.class, problemId);

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
            Problem problem = session.load(Problem.class, problemId);

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

        return session().createNamedQuery(ProblemQueries.GET_PROBLEM_FEEDBACK, SmallFeedbackDTO.class)
                .setParameter("id", problemId)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public String getProblemAnswer(long problemId) {

        return session().createNamedQuery(ProblemQueries.GET_PROBLEM_ANSWER, String.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean isAlreadyDecide(long problemId, long userId) {

        Session session = session();

        return !session.createNamedQuery(ProblemQueries.IS_ALREADY_DECIDED)
                .setParameter("problem", session.load(Problem.class, problemId))
                .setParameter("user", session.load(UserInfo.class, userId))
                .getResultList().isEmpty();
    }

    @Override
    public PageableEntity getSmallProblems(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"), counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                        ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getProblemsByDifficult(int start, int size, ProblemDifficult difficult, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        query.where(cb.equal(problem.get("difficult"), difficult));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"),counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity findProblemsByDifficult(int start, int size, String search, ProblemDifficult difficult, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        query.where(cb.and(cb.equal(problem.get("difficult"), difficult),
                cb.like(problem.get("title"), "%"+search.replace(" ", "%")+"%")));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"),counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity findSmallProblems(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        query.where(cb.like(problem.get("title"), "%"+search.replace(" ", "%")+"%"));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"),counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public FullProblemTransfer getOneProblem(long problemId) {

        FullProblemTransfer problem = session().createNamedQuery(ProblemQueries.GET_FULL_PROBLEM, FullProblemTransfer.class)
                .setParameter("id", problemId).stream().findFirst().orElse(null);
        if(problem == null){
            return null;
        }

        problem.setTags(new HashSet<>(getTagsByProblem(problemId)));

        return problem;
    }

    @Override
    public EditProblemDTO getEditProblem(long problemId) {

        EditProblemDTO problem = session().createNamedQuery(ProblemQueries.GET_EDIT_PROBLEM, EditProblemDTO.class)
                .setParameter("id", problemId).getResultList().stream().findFirst().orElse(null);

        problem.setTags(new HashSet<>(getTagsByProblem(problemId)));

        return problem;
    }

    @Override
    public List<SmallTagTransfer> getTagsByProblem(long problemId){

       return   session().createNamedQuery(ProblemQueries.GET_TAGS_BY_PROBLEMS, SmallTagTransfer.class)
                .setParameter("id", problemId)
                .getResultList();

    }

    @Override
    public FullSolutionTransfer getProblemSolution(long problemId){

        FullSolutionTransfer fullSolutionTransfer = session().createNamedQuery(ProblemQueries.GET_PROBLEM_SOLUTION, FullSolutionTransfer.class)
                .setParameter("id", problemId)
                .stream().findFirst().orElse(null);
        if(fullSolutionTransfer == null){
            return null;
        }

        List<CommonCommentTransfer> comments = getProblemComments(problemId);
        Set<CommonCommentTransfer> set = comments == null ? Collections.emptySet() : new TreeSet<>(comments);
        fullSolutionTransfer.setComments(set);

        return fullSolutionTransfer;

    }

    @Override
    public List<CommonCommentTransfer> getProblemComments(long problemId){

        Session session = session();
        Problem problem = session.load(Problem.class, problemId);

        return session.createNamedQuery(ProblemQueries.GET_COMMENTS_BY_PROBLEM, CommonCommentTransfer.class)
                .setParameter("problem", problem)
                .getResultList();

    }

    @Override
    public boolean updateSolution(long problemId, String solution) {
        try {
            Session session = session();

            Problem problem = session.load(Problem.class, problemId);

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_SOLUTION)
                    .setParameter("solution", solution)
                    .setParameter("problem", problem)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateAnswer(long problemId, String answer) {
        try {
            Session session = session();

            Problem problem = session.load(Problem.class, problemId);

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_SOLUTION)
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
            return session.createNamedQuery(ProblemQueries.DELETE_PROBLEM)
                    .setParameter("id", problemId)
                    .setParameter("user", session.load(UserInfo.class,userId))
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getProblemsNum() {
        return (long) session().createNamedQuery(ProblemQueries.COUNT_PROBLEMS, Object.class)
                .getSingleResult();
    }

    @Override
    public PageableEntity getSmallProblemsByTag(int start, int size, long tagID,
                                                            ProblemDifficult difficult, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemsTags> tags = problem.join("tags");
        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if(difficult != null)
            predicates.add(cb.equal(problem.get("difficult"), difficult));
        if(q != null)
            predicates.add(cb.like(problem.get("title"), "%"+q.replace(" ", "%")+"%"));
        predicates.add(cb.equal(tags.get("tagId"), tagID));

        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"),counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getSmallProblemsByUser(int start, int size, long userId,
                                                             ProblemDifficult difficult, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, UserInfo> creator = problem.join("creator");
        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if(difficult != null)
            predicates.add(cb.equal(problem.get("difficult"), difficult));
        if(q != null)
            predicates.add(cb.like(problem.get("title"), "%"+q.replace(" ", "%")+"%"));
        predicates.add(cb.equal(creator.get("userId"), userId));

        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"),counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getUserSolvedProblems(int start, int size, long userId, ProblemDifficult difficult, String q, String type, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Problem> problem = query.from(Problem.class);

        Join<Problem, ProblemsSolvedUsers> solvers = problem.join("solvers");
        Join<Problem, ProblemCounter> counter = problem.join("counter", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if(difficult != null)
            predicates.add(cb.equal(problem.get("difficult"), difficult));
        if(q != null)
            predicates.add(cb.like(problem.get("title"), "%"+q.replace(" ", "%")+"%"));
        predicates.add(cb.equal(solvers.get("user"), session.load(UserInfo.class, userId)));

        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        query.select(cb.count(problem.get("problemId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"),
                problem.get("createDate"), problem.get("rating"), counter.get("solved"), problem.get("difficult")));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(type)));
        }else{
            query.orderBy(cb.desc(problem.get(type)));
        }

        return new PageableEntity(attachTags.tags((List)
                        session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public boolean isProblemVoted(long problemId, long userId) {
        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        Problem problem = session.load(Problem.class, problemId);

        return  !session.createNamedQuery(ProblemQueries.IS_PROBLEM_VOTED, Long.class)
                .setParameter("problem", problem)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isProblemCommentVoted(long problemCommentId, long userId) {
        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        ProblemComment comment = session.load(ProblemComment.class, problemCommentId);

        return  !session.createNamedQuery(ProblemQueries.IS_COMMENT_PROBLEM_VOTED, Long.class)
                .setParameter("comment", comment)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean changeCommentRate(long commentId, int rate, long userId) {
        try {
            Session session = session();

            ProblemComment comment = session.load(ProblemComment.class, commentId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new ProblemCommentVoters(comment, user));

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_COMMENT_RATE)
                    .setParameter("id", commentId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean changeRate(long problemId, int rate, long userId) {
        try {
            Session session = session();

            Problem problem = session.load(Problem.class, problemId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new ProblemVoters(problem, user));

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_RATE)
                    .setParameter("id", problemId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
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
            UserInfo user = session.load(UserInfo.class, userId);

            ProblemComment problemComment = new ProblemComment(comment);
            problemComment.setProblem(problem);
            problemComment.setCreator(user);

            long problemCommentId = (long) session.save(problemComment);

            return getProblemComment(problemCommentId);
        }catch (Exception e){
            return null;
        }
    }

    public CommonCommentTransfer getProblemComment(long problemId){

        return session().createNamedQuery(ProblemQueries.GET_COMMENT, CommonCommentTransfer.class)
                .setParameter("id", problemId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {
        try {
            Session session = session();
            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_COMMENT)
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
            return session.createNamedQuery(ProblemQueries.REMOVE_PROBLEM_COMMENT)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .setParameter("id", commentId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }
}
