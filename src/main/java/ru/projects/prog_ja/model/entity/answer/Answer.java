package ru.projects.prog_ja.model.entity.answer;

import ru.projects.prog_ja.model.entity.questions.QuestionContent;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Answer")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "deleteAnswer", query = "delete from Answer a where a.answerId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "getFullAnswer", query = "select a from Answer a " +
                " left join fetch a.userInfo " +
                " where a.answerId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "getCommonAnswer", query = "select new ru.projects.prog_ja.dto.commons.CommonAnswerTranswer(" +
                " a.answerId, a.htmlContent,a.rating, a.right,  a.createDate, a.userInfo.userId, a.userInfo.login, a.userInfo.smallImagePath, a.userInfo.rating " +
                " ) from Answer a " +
                " where a.answerId = :id ")
})
public class Answer {

    public static final String DELETE_ANSWER = "deleteAnswer";
    public static final String GET_FULL_ANSWER = "getFullAnswer";
    public static final String GET_COMMON_ANSWER = "getCommonAnswer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", unique = true, nullable = false)
    private long answerId;

    @Column(name = "htmlContent")
    private String htmlContent;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "answer_user_fk"))
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_content_id", nullable = false, foreignKey = @ForeignKey(name = "question_content_answer_fk"))
    private QuestionContent questionContent;

    @Basic
    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Column(name = "rating")
    private long rating;

    @Column(name = "isright")
    private boolean right = false;

    public Answer(){}

    public Answer(String htmlContent) {
        this.htmlContent = htmlContent;
        this.createDate = new Date(new java.util.Date().getTime());
        rating = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return answerId == answer.answerId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(answerId);
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public QuestionContent getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
