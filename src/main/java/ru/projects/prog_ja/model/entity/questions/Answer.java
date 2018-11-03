package ru.projects.prog_ja.model.entity.questions;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Answer")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "deleteAnswer", query = "delete from Answer a where a.answerId = :id and a.userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "updateAnswerRate", query = "update Answer set rating = rating + :rate  where answerId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "getFullAnswer", query = "select a from Answer a " +
                " left join fetch a.userInfo " +
                " where a.answerId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "getCommonAnswer", query = "select new ru.projects.prog_ja.dto.commons.CommonAnswerTranswer(" +
                " a.answerId, a.htmlContent,a.rating,  a.createDate, a.userInfo.userId, a.userInfo.login, a.userInfo.smallImagePath, a.userInfo.rating " +
                " ) from Answer a " +
                " where a.answerId = :id ")
})
public class Answer {

    public static final String DELETE_ANSWER = "deleteAnswer";
    public static final String UPDATE_ANSWER_RATE = "updateAnswerRate";
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
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "question_answer_fk"))
    private Questions question;

    @Basic
    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Column(name = "rating")
    private long rating;

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

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
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

}
