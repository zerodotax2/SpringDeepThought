package ru.projects.prog_ja.model.entity.questions;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Questions")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "deleteQuestion", query = "delete from Questions q where q.questionId = :id and q.userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "findSmallQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer( " +
                " q.questionId,  q.title, q.createDate, q.rating, q.views, u.userId, u.smallImagePath, u.login, u.rating " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u" +
                " where lower(q.title) like lower(:search) "),
        @org.hibernate.annotations.NamedQuery(name = "findQuestions", query = "select new ru.projects.prog_ja.dto.commons.CommonQuestionTransfer( " +
                " q.questionId, q.title, q.createDate,  q.rating, q.views, u.userId, u.smallImagePath, u.login, u.rating, substring(q.questionContent.htmlContent, 0, 256) " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u" +
                " where lower(q.title) like lower(:search) "),
        @org.hibernate.annotations.NamedQuery(name = "getQuestions", query = "select new ru.projects.prog_ja.dto.commons.CommonQuestionTransfer( " +
                " q.questionId, q.title, q.createDate,  q.rating, q.views, u.userId, u.smallImagePath, u.login, u.rating, substring(q.questionContent.htmlContent, 0, 256) " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u"),
        @org.hibernate.annotations.NamedQuery(name = "getFullQuestion", query = "select q from Questions q " +
                " left join fetch q.questionContent as c" +
                " left join fetch q.answers " +
                " left join fetch q.userInfo as u" +
                " left join fetch q.tags as t " +
                " left join fetch t.tagId " +
                " where q.questionId = :id "),
        @NamedQuery(name = "getSmallQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer( " +
                " q.questionId, q.title, q.createDate, q.rating, q.views, u.userId, u.smallImagePath, u.login, u.rating " +
                " ) " +
                " from Questions q" +
                " left join q.userInfo as u "),
        @NamedQuery(name = "countQuestions", query = "select distinct count(q.questionId) from Questions q"),
        @NamedQuery(name = "updateQuestionRate", query = "update Questions set rating = rating + :rate where questionId = :id"),
        @NamedQuery(name = "updateRightAnswer", query = "update Questions set rightId = :right where questionId in " +
                " (select question from Answer where answerId = :id) " +
                " and userInfo = :user")
})
public class Questions {

    public static final String DELETE_QUESTION = "deleteQuestion";
    public static final String UPDATE_QUESTION_RATE = "updateQuestionRate";
    public static final String UPDATE_RIGHT_ANSWER = "updateQuestionRate";
    public static final String FIND_SMALL_QUESTIONS = "findSmallQuestions";
    public static final String FIND_QUESTIONS = "findQuestions";
    public static final String GET_QUESTIONS = "getQuestions";
    public static final String GET_SMALL_QUESTIONS = "getSmallQuestions";
    public static final String GET_FULL_QUESTION = "getFullQuestion";
    public static final String COUNT_QUESTIONS = "countQuestions";

    private long questionId;
    private String title;
    private Date createDate;
    private long rating;
    private long views;
    private QuestionContent questionContent;
    private UserInfo userInfo;
    private Set<QuestionsTags> tags;
    private List<Answer> answers;
    private boolean activated = true;
    private long rightId;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false, unique = true)
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "createDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Questions(){}

    public Questions(String title) {
        this.title = title;
        this.createDate = new Date(new java.util.Date().getTime());
        rating = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questions questions = (Questions) o;
        return questionId == questions.questionId &&
                Objects.equals(title, questions.title) &&
                Objects.equals(createDate, questions.createDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(questionId, title, createDate);
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "question_content_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "question_from_content_fk"))
    public QuestionContent getQuestionContent() {
        return this.questionContent;
    }

    public void setQuestionContent(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @OneToMany(mappedBy = "questionId")
    @Fetch(FetchMode.SELECT)
    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size = 10)
    public Set<QuestionsTags> getTags() {
        return tags;
    }

    public void setTags(Set<QuestionsTags> tags) {
        this.tags = tags;
    }

    @Basic
    @Column(name = "activated", nullable = false)
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Column(name = "rating")
    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    @Column(name = "right_id", nullable = true)
    public long getRightId() {
        return rightId;
    }

    public void setRightId(long rightId) {
        this.rightId = rightId;
    }

    @OneToMany(mappedBy = "question", orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Column(name = "views", nullable = false)
    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
