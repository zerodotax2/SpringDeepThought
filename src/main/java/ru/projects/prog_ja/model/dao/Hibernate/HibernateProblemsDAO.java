package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.ProblemsDAO;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.problems.ProblemContent;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;
import ru.projects.prog_ja.model.entity.problems.ProblemsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
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
    public void createProblem(long userId, String title, String content, List<Long> tags, ProblemDifficult difficult) {

        Session session = session();

        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null){
            return;
        }

        Problem problem = new Problem(title, difficult);
        problem.setCreator(user);

        Set<ProblemsTags> problemsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            problemsTags.add(new ProblemsTags(problem, tag));
        });
        if(problemsTags.size() < 3){
            return;
        }
        problem.setTags(problemsTags);

        ProblemContent problemContent = new ProblemContent(content);
        problem.setContent(problemContent);

        session.save(problem);
    }

    @Override
    public void updateProblem(long problemId, String title, String content, List<Long> tags, ProblemDifficult difficult) {

        Session session = session();

        Problem problem = session.createNamedQuery(Problem.GET_FULL_PROBLEM, Problem.class)
                .setParameter("id", problemId).stream().findFirst().orElse(null);
        if(problem == null)
            return;

        Set<ProblemsTags> problemsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            problemsTags.add(new ProblemsTags(problem, tag));
        });
        if(problemsTags.size() < 3){
            return;
        }
        problem.setTags(problemsTags);

        problem.setTitle(title);
        problem.setTags(problemsTags);
        problem.setDifficult(difficult);

        problem.getContent().setHtmlContent(content);

        session.save(problem);

    }

    @Override
    public void updateTitle(long problemId, String title) {

        session().createNamedQuery(Problem.UPDATE_PROBLEM_TITLE)
                .setParameter("id", problemId)
                .setParameter("title", title)
                .executeUpdate();

    }

    @Override
    public void updateContent(long problemId, String content) {

        session().createNamedQuery(Problem.UPDATE_PROBLEM_CONTENT)
                .setParameter("id", problemId)
                .setParameter("content", content)
                .executeUpdate();
    }

    @Override
    public void updateTags(long problemId, List<Long> tags) {
        Session session = session();

        Problem problem = session.find(Problem.class, problemId);
        if(problem == null){
            return;
        }

        Set<ProblemsTags> problemsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            problemsTags.add(new ProblemsTags(problem, tag));
        });
        if(problemsTags.size() < 3){
            return;
        }
        problem.setTags(problemsTags);

        session.save(problem);
    }

    @Override
    public void updateDifficult(long problemId, ProblemDifficult problemDifficult) {

        session().createNamedQuery(Problem.UPDATE_PROBLEM_DIFFICULT)
                .setParameter("difficult", problemDifficult)
                .setParameter("id", problemId)
                .executeUpdate();
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
    public List<SmallProblemTransfer> findSmallProblems(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallProblemTransfer> query = cb.createQuery(SmallProblemTransfer.class);
        Root<Problem> problem = query.from(Problem.class);

        query.select(cb.construct(SmallProblemTransfer.class, problem.get("problemId"), problem.get("title"), problem.get("createDate"), problem.get("rating"), problem.get("difficult")));
        query.where(cb.like(problem.get("title"), "%"+search+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(problem.get(orderField)));
        }else{
            query.orderBy(cb.desc(problem.get(orderField)));
        }

        return attachTags.tags(session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    //TODO
    @Override
    public FullProblemTransfer getOneProblem(long problemId) {

        Problem problem = session().createNamedQuery(Problem.GET_FULL_PROBLEM, Problem.class)
                .setParameter("id", problemId).stream().findFirst().orElse(null);
        if(problem == null){
            return null;
        }

        FullProblemTransfer fullProblemTransfer = new FullProblemTransfer(problem.getProblemId(), problem.getTitle(), problem.getCreateDate(), problem.getRating(),problem.getDifficult(), problem.getContent().getHtmlContent());

        Set<ProblemsTags> tagsSet  = problem.getTags();
        for(ProblemsTags tag1 : tagsSet){
            Tags tag = tag1.getTagId();
            fullProblemTransfer.getTags().add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
        }

        return fullProblemTransfer;
    }

    @Override
    public void deleteProblem(long problemId) {
        Session session = session();

        Problem problem = session.load(Problem.class, problemId);
        if(problem == null){
            return;
        }

        session.delete(problem);
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
}
