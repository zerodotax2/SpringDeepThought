package ru.projects.prog_ja.model.entity.support;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_questions")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getUserQuestion", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u" +
                " where uq.userQuestionId = : id"),
        @org.hibernate.annotations.NamedQuery(name = "getEntityUserQuestion", query = " select uq from UserQuestion uq" +
                " left join fetch uq.user u" +
                " where uq.userQuestionId = : id"),
        @org.hibernate.annotations.NamedQuery(name = "getUserQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u" +
                " order by uq.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "getNonAnsweredUserQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u " +
                " where uq.answer is null " +
                " order by uq.createDate desc "),
        @org.hibernate.annotations.NamedQuery(name = "getForumAnswer", query = "select new ru.projects.prog_ja.dto.smalls.SmallAnswerTransfer(" +
                " a.userForumAnswerId, a.text, a.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                ") from UserQuestion uq" +
                " left join uq.answer a " +
                " left join a.user u " +
                " where uq.answer is not null and uq.userQuestionId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "removeForumAnswer", query = "delete from UserForumAnswer where userForumAnswerId = :id and user = :user"),
        @org.hibernate.annotations.NamedQuery(name = "removeForumQuestion", query = "delete from UserQuestion where userQuestionId = :id and user = :user")
})
public class UserQuestion {

    public static final String GET_USER_QUESTIONS = "getUserQuestions";
    public static final String GET_ENTITY_USER_QUESTION = "getEntityUserQuestion";
    public static final String GET_NON_ANSWERED_USER_QUESTIONS = "getNonAnsweredUserQuestions";
    public static final String GET_USER_QUESTION = "getUserQuestion";
    public static final String GET_FORUM_ANSWER = "getForumAnswer";
    public static final String REMOVE_FORUM_QUESTION = "removeForumAnswer";
    public static final String REMOVE_FORUM_ANSWER = "removeForumQuestion";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_question_id", nullable = false, unique = true)
    private long userQuestionId;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_forum_question_fk"))
    private UserInfo user;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "answered")
    @JoinColumn(name = "user_forum_answer_id", unique = true, foreignKey = @ForeignKey(name = "user_forum_answer_fk"))
    private UserForumAnswer answer;

    public UserQuestion() {
    }

    public UserQuestion(String text, Date createDate) {
        this.text = text;
        this.createDate = createDate;
    }

    public long getUserQuestionId() {
        return userQuestionId;
    }

    public void setUserQuestionId(long userQuestionId) {
        this.userQuestionId = userQuestionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserForumAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(UserForumAnswer answer) {
        this.answer = answer;
    }
}
