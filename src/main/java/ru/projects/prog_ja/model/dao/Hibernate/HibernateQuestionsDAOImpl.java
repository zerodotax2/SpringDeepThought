package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.QuestionConverter;
import ru.projects.prog_ja.model.dao.QuestionsDAO;
import ru.projects.prog_ja.model.entity.questions.Answer;
import ru.projects.prog_ja.model.entity.questions.QuestionContent;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.questions.QuestionsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
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
    public FullQuestionTransfer createQuestion(String title, List<Long> tags, String htmlContent, long userId) {
        try {

            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return null;

            Questions questions = new Questions(title);

            /*Создаём обычный вопрос и ставим юзера, который его создал*/
            Set<QuestionsTags> questionsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag) -> {
                questionsTags.add(new QuestionsTags(questions, tag));
            });
            if(questionsTags.size() < 3){
                return null;
            }
            questions.setTags(questionsTags);

            questions.setUserInfo(user);

            /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
            QuestionContent questionContent = new QuestionContent(htmlContent);
            questions.setQuestionContent(questionContent);

          return getFullQuestion((long)session.save(questions));

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean deleteQuestion(long id, long userId) {

        try {
            Session session = session();
            return session.createNamedQuery(Questions.DELETE_QUESTION)
                    .setParameter("id", id)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateQuestionRate(long questionId, int rate, long userId) {
        try{
            return session().createNamedQuery(Questions.UPDATE_QUESTION_RATE)
                    .setParameter("id", questionId)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<SmallQuestionTransfer> findSmallQuestions(int start, int size, String search, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SmallQuestionTransfer> query =  cb.createQuery(SmallQuestionTransfer.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        return questionConverter.fullQuestion(question, question.getQuestionContent(), question.getAnswers(),
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

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
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

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"),
                user.get("userId"), user.get("smallImagePath"), user.get("login"), user.get("rating")));
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
    public boolean updateQuestion(long id, String title, List<Long> tags, String htmlContent, long userId){

        try{
            /*Создаём обычный вопрос и ставим юзера, который его создал*/
            Session session = session();

            Questions questions = session.createNamedQuery(Questions.GET_FULL_QUESTION, Questions.class)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst().orElse(null);
            if(questions == null || questions.getUserInfo().getUserId() != userId)
                return false;

            questions.setTitle(title);

            Set<QuestionsTags> questionsTags = new HashSet<>();
            session.byMultipleIds(Tags.class).multiLoad(tags).forEach((tag)->{
                questionsTags.add(new QuestionsTags(questions, tag));
            });
            if(questionsTags.size() < 3){
                return false;
            }
            questions.setTags(questionsTags);

            /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
            QuestionContent questionContent = questions.getQuestionContent();
            questionContent.setHtmlContent(htmlContent);

            session.saveOrUpdate(questions);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public CommonAnswerTransfer addAnswer(String htmlContent, long questionId, long userId) {

        try {
            Session session = session();

            /*Ищем контент к которому добавляем ответ, если его нет возвращаемся*/
            Questions question = session.load(Questions.class, questionId);
            if(question==null)
                return null;

            UserInfo user = session.load(UserInfo.class, userId);
            if(user == null)
                return null;

            /*Создаем новый ответ и устанавливаем юзера, который его написал, и контент, к которому он принадлежит*/
            Answer answer = new Answer(htmlContent);
            answer.setUserInfo(user);
            answer.setQuestion(question);

            return getAnswer((long)session.save(answer));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean updateAnswer(long id, String htmlContent, long userId) {
        try{
            Session session = session();
            Answer answer = session.createNamedQuery(Answer.GET_FULL_ANSWER, Answer.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if(answer == null || answer.getUserInfo().getUserId() != userId)
                return false;

            answer.setHtmlContent(htmlContent);

            session.saveOrUpdate(answer);
       }catch (Exception e){
           return false;
       }
       return true;
    }

    @Override
    public List<ViewAnswerTransfer> getSmallAnswersByUser(int start, int size, long userId, String type, int sort){

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ViewAnswerTransfer> query = cb.createQuery(ViewAnswerTransfer.class);
        Root<Questions> answerRoot = query.from(Questions.class);

        Join<Questions, Answer> qeustionAnswersJoin = answerRoot.join("answers", JoinType.LEFT);


        query.select(cb.construct(ViewAnswerTransfer.class,
                qeustionAnswersJoin.get("answerId"), answerRoot.get("questionId"), answerRoot.get("title"),
                qeustionAnswersJoin.get("createDate"), qeustionAnswersJoin.get("rating"), qeustionAnswersJoin.get("right")));
        query.where(cb.equal(qeustionAnswersJoin.get("userInfo"), session.load(UserInfo.class, userId)));

        if(sort == 0){
            query.orderBy(cb.asc(qeustionAnswersJoin.get(type)));
        }else{
            query.orderBy(cb.desc(qeustionAnswersJoin.get(type)));
        }

        return session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList();
    }

    @Override
    public boolean deleteAnswer(long id, long userId) {
        try{

            Session session = session();

            return  session.createNamedQuery(Answer.DELETE_ANSWER)
                    .setParameter("id", id)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateAnswerRate(long answerId, int rate, long userId) {
        try{
           return session().createNamedQuery(Answer.UPDATE_ANSWER_RATE)
                    .setParameter("id", answerId)
                    .setParameter("rate", rate)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return  false;
        }
    }

    @Override
    public CommonAnswerTransfer getAnswer(long id){
        return session().createNamedQuery(Answer.GET_COMMON_ANSWER, CommonAnswerTransfer.class)
                .setMaxResults(1)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean setRightAnswer(long answerId, long userId) {
        try{
            return session().createNamedQuery(Questions.UPDATE_RIGHT_ANSWER)
                    .setParameter("right", answerId)
                    .setParameter("id", answerId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getQuestionsNum() {
        return (long) session().createNamedQuery(Questions.COUNT_QUESTIONS, Object.class)
                .getSingleResult();
    }
}
