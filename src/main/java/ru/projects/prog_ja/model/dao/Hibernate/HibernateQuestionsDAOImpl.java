package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.AttachTagService;
import ru.projects.prog_ja.model.dao.Hibernate.helpers.QuestionConverter;
import ru.projects.prog_ja.model.dao.Hibernate.queries.QuestionQueries;
import ru.projects.prog_ja.model.dao.Hibernate.queries.TagQueries;
import ru.projects.prog_ja.model.dao.QuestionsDAO;
import ru.projects.prog_ja.model.entity.questions.*;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Scope(scopeName = "prototype")
public class HibernateQuestionsDAOImpl extends GenericDAO implements QuestionsDAO {

    private final String ID_COLUMN = "questionId";
    private final String ENTITIES_NAME = "questions";

    private final AttachTagService<QuestionsTags, Questions> attachTags;
    private final QuestionConverter questionConverter;

    @Autowired
    public HibernateQuestionsDAOImpl(SessionFactory sessionFactory,
                                     AttachTagService<QuestionsTags, Questions> attachTags,
                                     QuestionConverter questionConverter){
        super(sessionFactory);
        this.attachTags = attachTags;
        this.questionConverter = questionConverter;
    }

    @Override
    public FullQuestionTransfer createQuestion(String title, List<Long> tags, String htmlContent, long userId) {
        try {

            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            Questions questions = new Questions(title);

            /*Создаём обычный вопрос и ставим юзера, который его создал*/
            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            if(tagsList == null || tagsList.size() < 3){
                return null;
            }
            Set<QuestionsTags> tagsSet = new HashSet<>(tagsList.size());
            tagsList.forEach(tag -> tagsSet.add(new QuestionsTags(questions, tag)));
            questions.setTags(tagsSet);

            questions.setUserInfo(user);

            /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
            QuestionContent questionContent = new QuestionContent(htmlContent);
            questionContent.setQuestion(questions);
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
            return session.createNamedQuery(QuestionQueries.DELETE_QUESTION)
                    .setParameter("id", id)
                    .setParameter("user", session.load(UserInfo.class, userId))
                    .executeUpdate() != 0;

        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean isQuestionVoted(long questionId, long userId) {
        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        Questions question = session.load(Questions.class, questionId);

        return !session.createNamedQuery(QuestionQueries.IS_QUESTION_VOTED, Long.class)
                .setParameter("question", question)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isAnswerVoted(long answerId, long userId) {
        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        Answer answer = session.load(Answer.class, answerId);

        return !session.createNamedQuery(QuestionQueries.IS_ANSWER_VOTED, Long.class)
                .setParameter("answer", answer)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    @Override
    public boolean updateQuestionRate(long questionId, int rate, long userId) {
        try{
            Session session = session();

            Questions question = session.load(Questions.class, questionId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new QuestionVoters(question, user));

            return session.createNamedQuery(QuestionQueries.UPDATE_QUESTION_RATE)
                    .setParameter("id", questionId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getTitle(long questionId) {
        return session().createNamedQuery(QuestionQueries.GET_TITLE, String.class)
                .setParameter("id", questionId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeCommentTemplateDTO getNoticeCommentTemplate(long commentId) {

        return session().createNamedQuery(QuestionQueries.GET_NOTICE_COMMENT_TEMPLATE, NoticeCommentTemplateDTO.class)
                .setParameter("id", commentId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeEntityTemplateDTO getNoticeTemplate(long questionId) {

        return session().createNamedQuery(QuestionQueries.GET_NOTICE_TEMPLATE, NoticeEntityTemplateDTO.class)
                .setParameter("id", questionId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Long> getTagsByQuestion(long questionId) {

        return session().createNamedQuery(QuestionQueries.GET_TAGS_BY_QUESTION, Long.class)
                .setParameter("id", questionId).getResultList();
    }

    @Override
    public PageableEntity findSmallQuestions(int start, int size, String search, String orderField, int sort) {

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.where(cb.like(question.get("title"), "%"+query+"%"));

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"), user.get("login"), user.get("smallImagePath"),  user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity findQuestions(int start, int size, String search, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);

        query.where(cb.like(question.get("title"), "%"+query+"%"));

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"),  user.get("login"), user.get("smallImagePath"), user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public FullQuestionTransfer getFullQuestion(long id){

        FullQuestionTransfer question = session().createNamedQuery(QuestionQueries.GET_FULL_QUESTION, FullQuestionTransfer.class)
                .setParameter("id", id).getResultList()
                .stream().findFirst().orElse(null);
        if(question == null){
            return null;
        }

        List<CommonAnswerTransfer> answers = getQuestionAnswers(id);
        if(answers == null)
            question.setAnswers(Collections.emptySet());
        else
            question.setAnswers(new TreeSet<>(answers));

        List<SmallTagTransfer> tags = getSmallQuestionTags(id);
        if(tags == null)
            question.setTags(Collections.emptySet());
        else
            question.setTags(new HashSet<>(tags));

        return question;
    }

    @Override
    public List<CommonAnswerTransfer> getQuestionAnswers(long questionId) {

        Session session = session();
        Questions question = session.load(Questions.class, questionId);

        return session.createNamedQuery(QuestionQueries.GET_QUESTION_ANSWERS, CommonAnswerTransfer.class)
                .setParameter("question", question)
                .getResultList();
    }

    @Override
    public List<SmallTagTransfer> getSmallQuestionTags(long questionId) {

        Session session = session();
        Questions question = session.load(Questions.class, questionId);

        return session.createNamedQuery(QuestionQueries.GET_SMALL_QUESTION_TAGS, SmallTagTransfer.class)
                .setParameter("question", question)
                .getResultList();

    }

    @Override
    public PageableEntity getQuestions(int start, int size, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"), user.get("login"), user.get("smallImagePath"), user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getSmallQuestions(int start, int size, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"),user.get("login"), user.get("smallImagePath"),  user.get("rating")));


        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getCommonQuestionsByTag(int start, int size, long tagID, String q, String orderField, int sort){
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionContent> content = question.join("questionContent", JoinType.LEFT);
        Join<Questions, QuestionsTags> tags = question.join("tags");

        if(q!=null)
            query.where(cb.and(cb.equal(tags.get("tagId"), tagID),
                    cb.like(question.get("title"), "%"+q.replace(" ", "%")+"%")));
        else
            query.where(cb.equal(tags.get("tagId"), tagID));

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(CommonQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"),user.get("login"), user.get("smallImagePath"),  user.get("rating"), cb.substring(content.get("htmlContent"), 0, 256)));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getSmallQuestionsByUser(int start, int size, long userId, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);

        if(q!=null)
            query.where(cb.and(cb.equal(user.get("userId"), userId),
                    cb.like(question.get("title"), "%"+q.replace(" ", "%")+"%")));
        else
            query.where(cb.equal(user.get("userId"), userId));

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"),  user.get("login"),user.get("smallImagePath"), user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public PageableEntity getSmallQuestionsByTag(int start, int size, long tagID, String q, String orderField, int sort) {
        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object> query =  cb.createQuery(Object.class);
        Root<Questions> question = query.from(Questions.class);

        Join<Questions, UserInfo> user = question.join("userInfo", JoinType.LEFT);
        Join<Questions, QuestionsTags> tags = question.join("tags");
        if(q!=null)
            query.where(cb.and(cb.equal(tags.get("tagId"), tagID),
                    cb.like(question.get("title"), "%"+q.replace(" ", "%")+"%")));
        else
            query.where(cb.equal(tags.get("tagId"), tagID));

        query.select(cb.count(question.get("questionId")));
        long count = (Long) session.createQuery(query).getResultList().stream().findFirst().orElse((long)0);

        query.select(cb.construct(SmallQuestionTransfer.class, question.get("questionId"), question.get("title"),
                question.get("createDate"), question.get("rating"), question.get("views"), question.get("rightId"),
                user.get("userId"),user.get("login"), user.get("smallImagePath"),  user.get("rating")));

        if(sort == 0){
            query.orderBy(cb.asc(question.get(orderField)));
        }else{
            query.orderBy(cb.desc(question.get(orderField)));
        }

        return new PageableEntity(attachTags.tags(
                (List)session.createQuery(query).setFirstResult(start).setMaxResults(size).getResultList(),
                ID_COLUMN, ENTITIES_NAME), count);
    }

    @Override
    public boolean updateQuestion(long id, String title, List<Long> tags, String htmlContent, long userId){

        try{
            /*Создаём обычный вопрос и ставим юзера, который его создал*/
            Session session = session();

            Questions questions = session.createNamedQuery(QuestionQueries.GET_UPDATE_ENTITY_QUESTION, Questions.class)
                    .setParameter("id", id)
                    .getResultList().stream().findFirst().orElse(null);
            if(questions == null || questions.getUserInfo().getUserId() != userId)
                return false;

            questions.setTitle(title);

            List<Tags> tagsList = session.createNamedQuery(TagQueries.GET_TAGS_BY_IDS, Tags.class)
                    .setParameterList("tags", tags).getResultList();
            questions.setTags(updateTags(questions, tagsList));

            /*Создаём контент для вопроса и заполняем его изображениями если они есть*/
            QuestionContent questionContent = questions.getQuestionContent();
            questionContent.setHtmlContent(htmlContent);

            session.save(questions);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    private Set<QuestionsTags> updateTags(Questions question, List<Tags> newTags){

        Set<QuestionsTags> oldTags = question.getTags();

        if(newTags == null || newTags.size() < 3){
            return oldTags;
        }

        /*
         * Перебираем по новым тегам, если есть совпадение удаляем из старых и новых тегов
         * */
        for(Iterator<Tags> i = newTags.iterator(); i.hasNext();){

            Tags t = i.next();

            for(Iterator<QuestionsTags> j = oldTags.iterator(); j.hasNext();){
                QuestionsTags old = j.next();
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
        for(QuestionsTags tag: oldTags)
            tagsToDelete.add(tag.getTagId());
        if(oldTags.size() > 0)
            session().createNamedQuery(QuestionQueries.REMOVE_QUESTION_TAGS)
                .setParameterList("tags", tagsToDelete).executeUpdate();

        /*
         * Всё что осталось в новых тегах добавляем
         * */
        Set<QuestionsTags> tagsToAdd = new HashSet<>(newTags.size());
        for(int i = 0; i < newTags.size(); i++)
            tagsToAdd.add(new QuestionsTags(question, newTags.get(i)));

        return tagsToAdd;
    }

    @Override
    public CommonAnswerTransfer addAnswer(String htmlContent, long questionId, long userId) {

        try {
            Session session = session();

            /*Ищем контент к которому добавляем ответ, если его нет возвращаемся*/
            Questions question = session.load(Questions.class, questionId);
            UserInfo user = session.load(UserInfo.class, userId);

            /*Создаем новый ответ и устанавливаем юзера, который его написал, и контент, к которому он принадлежит*/
            Answer answer = new Answer(htmlContent);
            answer.setUserInfo(user);
            answer.setQuestion(question);

            long id = (long)session.save(answer);

            return getAnswer(id);
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
    public List<ViewAnswerTransfer> getSmallAnswersByUser(int start, int size, long userId, String q, String type, int sort){

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ViewAnswerTransfer> query = cb.createQuery(ViewAnswerTransfer.class);
        Root<Questions> answerRoot = query.from(Questions.class);

        Join<Questions, Answer> qeustionAnswersJoin = answerRoot.join("answers", JoinType.LEFT);


        query.select(cb.construct(ViewAnswerTransfer.class,
                qeustionAnswersJoin.get("answerId"), answerRoot.get("questionId"), answerRoot.get("title"),
                qeustionAnswersJoin.get("createDate"), qeustionAnswersJoin.get("rating"), answerRoot.get("rightId")));

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
            Session session = session();

            Answer answer = session.load(Answer.class, answerId);
            UserInfo user = session.load(UserInfo.class, userId);

            session.save(new AnswerVoters(answer, user));

           return session.createNamedQuery(Answer.UPDATE_ANSWER_RATE)
                    .setParameter("id", answerId)
                    .setParameter("rate",(long) rate)
                    .setParameter("user", user)
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
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);
            Answer answer = session.load(Answer.class, answerId);

            return session.createNamedQuery(QuestionQueries.UPDATE_RIGHT_ANSWER)
                    .setParameter("right", answerId)
                    .setParameter("answer", answer)
                    .setParameter("user", user)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public long getQuestionsNum() {
        return (long) session().createNamedQuery(QuestionQueries.COUNT_QUESTIONS, Object.class)
                .getSingleResult();
    }
}
