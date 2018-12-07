package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.model.dao.Hibernate.queries.StatsQueries;
import ru.projects.prog_ja.model.dao.StatsDAO;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

@Repository
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateStatsDAOImpl extends GenericDAO implements StatsDAO {

    @Autowired
    public HibernateStatsDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean addProblemAttempts(long problemId, int attempts) {

        return updateProblem(StatsQueries.INCREMENT_PROBLEM_ATTEMPTS, problemId, attempts);
    }

    @Override
    public boolean addProblemSolved(long problemId, int solved) {

        return updateProblem(StatsQueries.INCREMENT_PROBLEM_SOLVED, problemId, solved);
    }

    @Override
    public boolean addTagFacts(long tagId, int facts) {

        return updateTag(StatsQueries.INCREMENT_TAG_FACTS, tagId, facts);
    }

    @Override
    public boolean addTagArticles(long tagId, int articles) {

        return updateTag(StatsQueries.INCREMENT_TAG_ARTICLES, tagId, articles);
    }

    @Override
    public boolean addTagQuestions(long tagId, int questions) {

        return updateTag(StatsQueries.INCREMENT_TAG_QUESTIONS, tagId, questions);
    }

    @Override
    public boolean addTagProblems(long tagId, int problems) {

        return updateTag(StatsQueries.INCREMENT_TAG_PROBLEMS, tagId, problems);
    }

    @Override
    public boolean addTagUsers(long tagId, int users) {

        return updateTag(StatsQueries.INCREMENT_TAG_USERS, tagId, users);
    }

    @Override
    public boolean addUserArticles(long userId, int articles) {

        return updateUser(StatsQueries.INCREMENT_USER_ARTICLES, userId, articles);
    }

    @Override
    public boolean addUserQuestions(long userId, int questions) {

        return updateUser(StatsQueries.INCREMENT_USER_QUESTIONS, userId, questions);
    }

    @Override
    public boolean addUserAnswers(long userId, int answers) {

        return updateUser(StatsQueries.INCREMENT_USER_ANSWERS, userId, answers);
    }

    @Override
    public boolean addUserProblemsSolved(long userId, int problemsSolved) {

        return updateUser(StatsQueries.INCREMENT_USER_PROBLEMS_SOLVED, userId, problemsSolved);
    }

    @Override
    public boolean addUserProblemsCreated(long userId, int problemsCreated) {

        return updateUser(StatsQueries.INCREMENT_USER_PROBLEMS_CREATED, userId, problemsCreated);
    }

    @Override
    public boolean addUserComments(long userId, int comments) {

        return updateUser(StatsQueries.INCREMENT_USER_COMMENTS, userId, comments);
    }

    @Override
    public boolean addUserFacts(long userId, int facts) {

        return updateUser(StatsQueries.INCREMENT_USER_FACTS, userId, facts);
    }

    @Override
    public boolean addUserTags(long userId, int tags) {

        return updateUser(StatsQueries.INCREMENT_USER_TAGS, userId, tags);
    }

    @Override
    public boolean addUserNotice(long userId, int notices) {
        return updateUser(StatsQueries.INCREMENT_USER_NOTICES,userId, notices);
    }

    private boolean updateTag(String queryName, long tagId, int num){

        Session session = session();
        Tags tag  = session.load(Tags.class, tagId);

        return session.createNamedQuery(queryName)
                .setParameter("tag", tag)
                .setParameter("num",(long) num)
                .executeUpdate() != 0;
    }

    private boolean updateUser(String queryName, long userId, int num){

        Session session = session();

        UserInfo user = session.load(UserInfo.class, userId);

        return session.createNamedQuery(queryName)
                .setParameter("user", user)
                .setParameter("num",(long) num)
                .executeUpdate() != 0;
    }

    private boolean updateProblem(String queryName, long problemId, int num){

        Session session = session();
        Problem problem = session.load(Problem.class, problemId);

        return session.createNamedQuery(queryName)
                .setParameter("problem", problem)
                .setParameter("num",(long) num)
                .executeUpdate() != 0;
    }
}
