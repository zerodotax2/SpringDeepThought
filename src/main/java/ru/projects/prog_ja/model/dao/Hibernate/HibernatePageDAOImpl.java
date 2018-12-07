package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.model.dao.Hibernate.queries.*;
import ru.projects.prog_ja.model.dao.PageDAO;

@Repository
@Scope("prototype")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HibernatePageDAOImpl extends GenericDAO implements PageDAO {

    @Autowired
    public HibernatePageDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public long countArticles() {
        return  session().createNamedQuery(ArticleQueries.COUNT_ARTICLES, Long.class)
                .getResultList()
                .stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countQuestions() {
        return session().createNamedQuery(QuestionQueries.COUNT_QUESTIONS, Long.class)
                .getResultList()
                .stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countProblems() {
        return session().createNamedQuery(ProblemQueries.COUNT_PROBLEMS, Long.class)
                .getResultList().stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countFacts() {
        return session().createNamedQuery(FactQueries.COUNT_FACTS, Long.class)
                .getResultList()
                .stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countTags() {
        return session().createNamedQuery(TagQueries.COUNT_TAGS, Long.class)
                .getResultList()
                .stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countUsers() {
        return session().createNamedQuery(UserQueries.COUNT_USERS, Long.class)
                .getResultList()
                .stream().findFirst().orElse((long) 0);
    }
}
