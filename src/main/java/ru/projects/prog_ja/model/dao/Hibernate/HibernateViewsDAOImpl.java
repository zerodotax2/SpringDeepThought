package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.model.dao.Hibernate.queries.ArticleQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.ProblemQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.QuestionQueries;
import ru.projects.prog_ja.model.dao.ViewsDAO;

@Repository
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateViewsDAOImpl extends GenericDAO implements ViewsDAO {

    public HibernateViewsDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean addArticleView(long articleId, int views) {

        return session().createNamedQuery(ArticleQueries.UPDATE_ARTICLE_VIEW)
                .setParameter("view", (long) views)
                .setParameter("id", articleId)
                .executeUpdate() != 0;
    }

    @Override
    public boolean addQuestionView(long questionId, int views) {

        return session().createNamedQuery(QuestionQueries.UPDATE_QUESTION_VIEW)
                .setParameter("view", (long) views)
                .setParameter("id", questionId)
                .executeUpdate() != 0;
    }

    @Override
    public boolean addProblemView(long problemId, int views) {

        return session().createNamedQuery(ProblemQueries.UPDATE_PROBLEM_VIEW)
                .setParameter("view", (long) views)
                .setParameter("id", problemId)
                .executeUpdate() != 0;
    }
}
