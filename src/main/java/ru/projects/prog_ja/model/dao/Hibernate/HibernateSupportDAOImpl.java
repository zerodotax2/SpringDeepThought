package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.full.FullForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.queries.ForumQuestionsQueries;
import ru.projects.prog_ja.model.dao.SupportDAO;
import ru.projects.prog_ja.model.entity.support.UserForumAnswer;
import ru.projects.prog_ja.model.entity.support.UserQuestion;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.Date;
import java.util.List;

@Repository
@Scope("prototype")
public class HibernateSupportDAOImpl extends GenericDAO implements SupportDAO {

    public HibernateSupportDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public FullForumAnswer addForumQuestion(String text, long userID) {
        try {

            Session session = session();

            UserInfo user = session.load(UserInfo.class, userID);

            UserQuestion userQuestion = new UserQuestion(text, new Date());
            userQuestion.setUser(user);

            long id = (long) session.save(userQuestion);

            return new FullForumAnswer(getSmallQuestion(id),null);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<SmallUserQuestionTransfer> getForumQuestions(int start, int size) {

        return session().createNamedQuery(ForumQuestionsQueries.GET_USER_QUESTIONS, SmallUserQuestionTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<SmallUserQuestionTransfer> getNonAnsweredForumQuestions(int start, int size) {

        return session().createNamedQuery(ForumQuestionsQueries.GET_NON_ANSWERED_USER_QUESTIONS, SmallUserQuestionTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public boolean answerForumQuestion(long userQuestionId, String text, long userId) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            UserQuestion userQuestion = getEntityQuestion(userQuestionId);
            if(userQuestion == null)
                return false;

            UserForumAnswer userForumAnswer = new UserForumAnswer(text, new Date());
            userForumAnswer.setUser(user);
            userForumAnswer.setForumQuestion(userQuestion);

            userQuestion.setAnswer(userForumAnswer);

            session.save(userQuestion);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean removeForumQuestion(long id, long userId) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

           return session.createNamedQuery(ForumQuestionsQueries.REMOVE_FORUM_QUESTION)
                   .setParameter("id", id)
                   .setParameter("user", user)
                   .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeForumAnswer(long id, long userId) {
        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

           return session.createNamedQuery(ForumQuestionsQueries.REMOVE_FORUM_ANSWER)
                   .setParameter("id", id)
                   .setParameter("user", user)
                   .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    private UserQuestion getEntityQuestion(long id){
        return session().createNamedQuery(ForumQuestionsQueries.GET_ENTITY_USER_QUESTION, UserQuestion.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public SmallUserQuestionTransfer getSmallQuestion(long id) {

        return session().createNamedQuery(ForumQuestionsQueries.GET_USER_QUESTION, SmallUserQuestionTransfer.class)
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public SmallForumAnswer getSmallAnswer(long id) {
        return session().createNamedQuery(ForumQuestionsQueries.GET_FORUM_ANSWER, SmallForumAnswer.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public FullForumAnswer getOneQuestion(long id) {

        return new FullForumAnswer(getSmallQuestion(id), getSmallAnswer(id));
    }

    @Override
    public NoticeEntityTemplateDTO getNoticeTemplate(long questionId) {

        return session().createNamedQuery(ForumQuestionsQueries.GET_NOTICE_TEMPLATE, NoticeEntityTemplateDTO.class)
                .setParameter("id", questionId)
                .getResultList().stream().findFirst().orElse(null);
    }
}
