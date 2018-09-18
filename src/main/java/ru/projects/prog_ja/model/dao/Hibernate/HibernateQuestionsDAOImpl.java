package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.QuestionConverter;
import ru.projects.prog_ja.model.dao.QuestionsDAO;
import ru.projects.prog_ja.model.entity.answer.Answer;
import ru.projects.prog_ja.model.entity.questions.QuestionContent;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.questions.QuestionsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateQuestionsDAOImpl extends GenericDAO implements QuestionsDAO {

    private final String ID_COLUMN = "questionId";
    private final String ENTITIES_NAME = "questions";

    private final AttachTagService<QuestionsTags> attachTags;
    private final QuestionConverter questionConverter;

    public HibernateQuestionsDAOImpl(@Autowired SessionFactory sessionFactory,
                                     @Autowired AttachTagService<QuestionsTags> attachTags,
                                     @Autowired QuestionConverter questionConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.questionConverter = questionConverter;
    }

    @Override
    public void createQuestion(String title, List<Long> tags, String htmlContent, long userId) {

        Session session = session();

        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null)
            return;

        Questions questions = new Questions(title);

        /*Создаём обычный вопрос и ставим юзера, который его создал*/
        Set<QuestionsTags> questionsTags = new HashSet<>();
        session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
           questionsTags.add(new QuestionsTags(questions, tag));
        });
        if(questionsTags.size() < 3){
            return;
        }
        questions.setTags(questionsTags);

        questions.setUserInfo(user);

        /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
        QuestionContent questionContent = new QuestionContent(htmlContent);
        questions.setQuestionContent(questionContent);

        session().save(questions);
    }

    @Override
    public void deleteQuestion(long id) {

        session().createNamedQuery(Questions.DELETE_QUESTION).setParameter("id", id).executeUpdate();

    }

    @Override
    public List<SmallQuestionTransfer> findSmallQuestions(int start, int size, String search, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallQuestionTransfer> query =  cb.createQuery(SmallQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating")));

        query.where(cb.like(question.get("title"), "%"+query+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonQuestionTransfer> findQuestions(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonQuestionTransfer> query =  cb.createQuery(CommonQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));

        query.where(cb.like(question.get("title"), "%"+query+"%"));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public FullQuestionTransfer getFullQuestion(long id){

        Questions question = session().createNamedQuery(Questions.GET_FULL_QUESTION, Questions.class)
                .setParameter("id", id).getResultList()
                .stream().findFirst().orElse(null);

        if(question == null){
            return null;
        }

        return questionConverter.fullQuestion(question, question.getQuestionContent(), question.getQuestionContent().getAnswers(),
                question.getTags(), question.getUserInfo());
    }

    @Override
    public List<CommonQuestionTransfer> getQuestions(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonQuestionTransfer> query =  cb.createQuery(CommonQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallQuestionTransfer> getSmallQuestions(int start, int size, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallQuestionTransfer> query =  cb.createQuery(SmallQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating")));


        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<CommonQuestionTransfer> getCommonQuestionsByTag(int start, int size, long tagID, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CommonQuestionTransfer> query =  cb.createQuery(CommonQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);
        Join<Questions, QuestionsTags> tags = question.join("tags");

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));
        query.where(cb.equal(tags.get("tagId"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallQuestionTransfer> getSmallQuestionsByUser(int start, int size, long userId, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallQuestionTransfer> query =  cb.createQuery(SmallQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating")));
        query.where(cb.equal(user.get("userId"), userId));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public List<SmallQuestionTransfer> getSmallQuestionsByTag(int start, int size, long tagID, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallQuestionTransfer> query =  cb.createQuery(SmallQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionsTags> tags = question.join("tags");

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"), question.get("createDate"), question.get("rating"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating")));
        query.where(cb.equal(tags.get("tagid"), tagID));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return attachTags.tags(  session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME);
    }

    @Override
    public void updateQuestion(long id, String title, List<Long> tags, String htmlContent) {
        /*Создаём обычный вопрос и ставим юзера, который его создал*/
        Session session = session();

        Questions questions = session.createNamedQuery(Questions.GET_FULL_QUESTION, Questions.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
        if(questions == null)
            return;

        questions.setTitle(title);

        Set<QuestionsTags> questionsTags = new HashSet<>();
        session().byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
            questionsTags.add(new QuestionsTags(questions, tag));
        });
        if(questionsTags.size() < 3){
            return;
        }
        questions.setTags(questionsTags);

        /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
        QuestionContent questionContent = questions.getQuestionContent();
        questionContent.setHtmlContent(htmlContent);

        session().save(questions);
    }

    @Override
    public long addAnswer(String htmlContent, long questionContentId, long userId) {

        Session session = session();

        /*Ищем контент к которому добавляем ответ, если его нет возвращаемся*/
        QuestionContent content = session.load(QuestionContent.class, questionContentId);
        if(content==null)
            return 0;

        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null)
            return 0;

        /*Создаем новый ответ и устанавливаем юзера, который его написал, и контент, к которому он принадлежит*/
        Answer answer = new Answer(htmlContent);
        answer.setUserInfo(user);
        answer.setQuestionContent(content);

        return (long) session().save(answer);
    }

    @Override
    public void updateAnswer(long id, String htmlContent) {
        Answer answer = session().createNamedQuery(Answer.GET_FULL_ANSWER, Answer.class)
                .setParameter("id", id)
                .getSingleResult();
        if(answer == null)
            return;

        answer.setHtmlContent(htmlContent);

        session().save(answer);
    }

    @Override
    public void deleteAnswer(long id) {
        session().createNamedQuery(Answer.DELETE_ANSWER).setParameter("id", id).executeUpdate();
    }

    @Override
    public CommonAnswerTransfer getAnswer(long id){
        return session().createNamedQuery(Answer.GET_COMMON_ANSWER, CommonAnswerTransfer.class)
                .setMaxResults(1)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public long getQuestionsNum() {
        return (long) session().createNamedQuery(Questions.COUNT_QUESTIONS, Object.class)
                .getSingleResult();
    }
}
