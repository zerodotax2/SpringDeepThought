package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.model.dao.Hibernate.queries.*;
import ru.projects.prog_ja.model.dao.RatingDAO;
import ru.projects.prog_ja.model.entity.articles.ArticleComments;
import ru.projects.prog_ja.model.entity.articles.ArticleInfo;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.problems.ProblemComment;
import ru.projects.prog_ja.model.entity.questions.Answer;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.tags.Tags;

@Repository
@Scope("prototype")
@Transactional
public class HibernateRatingDAOImpl extends GenericDAO implements RatingDAO {

    @Autowired
    public HibernateRatingDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean updateUserRate(long userId, int rate){
        try {
            return session().createNamedQuery(UserQueries.UPDATE_RATE)
                    .setParameter("id", userId)
                    .setParameter("rate",(long) rate)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateArticleOwnerRate(long articleId, int rate, long userId){
        try {
            Session session = session();
            ArticleInfo article = session.load(ArticleInfo.class, articleId);

            return session.createNamedQuery(ArticleQueries.UPDATE_ARTICLE_OWNER_RATE)
                    .setParameter("article", article)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateArticleCommentOwnerRate(long commentId, int rate, long userId){

        try {
            Session session = session();
            ArticleComments comment = session.load(ArticleComments.class, commentId);

            return session.createNamedQuery(ArticleQueries.UPDATE_ARTICLE_COMMENT_OWNER_RATE)
                    .setParameter("comment", comment)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateQuestionOwnerRate(long questionId, int rate, long userId){
        try {
            Session session = session();
            Questions question = session.load(Questions.class, questionId);

            return session.createNamedQuery(QuestionQueries.UPDATE_QUESTION_OWNER_RATE)
                    .setParameter("question", question)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateAnswerOwnerRate(long answerId, int rate, long userId) {

        try {
            Session session = session();
            Answer answer = session.load(Answer.class, answerId);

            return session.createNamedQuery(QuestionQueries.UPDATE_ANSWER_OWNER_RATE)
                    .setParameter("answer", answer)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProblemOwnerRate(long problemId, int rate, long userId){

        try {
            Session session = session();
            Problem problem = session.load(Problem.class, problemId);

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_OWNER_RATE)
                    .setParameter("problem", problem)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProblemCommentOwnerRate(long commentId, int rate, long userId){

        try {
            Session session = session();
            ProblemComment comment = session.load(ProblemComment.class, commentId);

            return session.createNamedQuery(ProblemQueries.UPDATE_PROBLEM_COMMENT_OWNER_RATE)
                    .setParameter("comment", comment)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", userId)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateFactOwnerRate(long factId, int rate, long userId) {

        try {
            Session session = session();
            Facts fact = session.load(Facts.class, factId);

            return session.createNamedQuery(FactQueries.UPDATE_FACT_OWNER_RATE)
                    .setParameter("fact", fact)
                    .setParameter("rate",(long) rate)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTagOwnerRate(long tagId, int rate, long userId){

        try {
            Session session = session();
            Tags tag = session.load(Tags.class, tagId);

            return session.createNamedQuery(TagQueries.UPDATE_TAG_OWNER_RATE)
                    .setParameter("tag", tag)
                    .setParameter("rate",(long) rate)
                    .executeUpdate() != 0;
        }catch (Exception e) {
            return false;
        }
    }
}
