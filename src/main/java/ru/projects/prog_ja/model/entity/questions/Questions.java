package ru.projects.prog_ja.model.entity.questions;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Questions")
@NamedQueries({
        @NamedQuery(name = "deleteQuestion", query = "delete from Questions q where q.questionId = :id and q.userInfo = :user"),
        @NamedQuery(name = "findSmallQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer( " +
                " q.questionId,  q.title, q.createDate, q.rating, q.views, q.rightId, u.userId, u.login, u.smallImagePath,  u.rating " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u" +
                " where lower(q.title) like lower(:search) "),
        @NamedQuery(name = "findQuestions", query = "select new ru.projects.prog_ja.dto.commons.CommonQuestionTransfer( " +
                " q.questionId, q.title, q.createDate,  q.rating, q.views, q.rightId, u.userId,u.login, u.smallImagePath,  u.rating, substring(q.questionContent.htmlContent, 0, 256) " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u" +
                " where lower(q.title) like lower(:search) "),
        @NamedQuery(name = "getQuestions", query = "select new ru.projects.prog_ja.dto.commons.CommonQuestionTransfer( " +
                " q.questionId, q.title, q.createDate,  q.rating, q.views, q.rightId, u.userId,u.login, u.smallImagePath,  u.rating, substring(q.questionContent.htmlContent, 0, 256) " +
                " ) " +
                " from Questions q " +
                " left join q.userInfo as u"),
        @NamedQuery(name = "getFullQuestion", query = "select new ru.projects.prog_ja.dto.full.FullQuestionTransfer(" +
                " q.questionId, q.title, q.createDate, q.rating, q.views, q.rightId, u.userId, u.login, u.smallImagePath, u.rating, c.htmlContent  " +
                " ) from Questions q" +
                " left join q.questionContent as c" +
                " left join q.userInfo as u" +
                " where q.questionId = :id "),
        @NamedQuery(name = "getSmallQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer( " +
                " q.questionId, q.title, q.createDate, q.rating, q.views, q.rightId, u.userId,  u.login, u.smallImagePath, u.rating " +
                " ) " +
                " from Questions q" +
                " left join q.userInfo as u "),
        @NamedQuery(name = "countQuestions", query = "select distinct count(q.questionId) from Questions q group by q.questionId"),
        @NamedQuery(name = "updateQuestionRate", query = "update Questions set rating = rating + :rate where questionId = :id and userInfo != :user"),
        @NamedQuery(name = "updateRightAnswer", query = "update Questions set rightId = :right where :answer in elements(answers) and userInfo = :user"),
        @NamedQuery(name = "getQuestionTitle", query = "select title from Questions where questionId = :id"),
        @NamedQuery(name = "updateQuestionOwnerRate", query = "update UserInfo set rating = rating + :rate" +
                " where :question in elements(userQuestions) and userId != :user"),
        @NamedQuery(name = "updateAnswerOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :answer in elements(answers) and userId != :user"),
        @NamedQuery(name = "updateQuestionView", query = "update Questions set views = views + :view where questionId = :id"),
        @NamedQuery(name = "getQuestionNoticeTemplate", query = "select new ru.projects.prog_ja.dto.NoticeEntityTemplateDTO (" +
                "  u.userId, q.title " +
                " ) from Questions q left join q.userInfo u where q.questionId = :id"),
        @NamedQuery(name = "getQuestionNoticeCommentTemplate", query = "select new ru.projects.prog_ja.dto.NoticeCommentTemplateDTO (" +
                " q.questionId, u.userId, q.title " +
                " ) from Answer a left join a.question q left join a.userInfo u where a.answerId = :id"),
        @NamedQuery(name = "getTagsByQuestion", query = "select t.tagId from Questions q left join q.tags qt left join qt.tagId t where q.questionId = :id"),
        @NamedQuery(name = "getQuestionAnswers", query = "select new ru.projects.prog_ja.dto.commons.CommonAnswerTransfer(" +
                " a.answerId, a.htmlContent, a.rating, a.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from Answer a " +
                " left join a.userInfo u " +
                " where a.question = :question"),
        @NamedQuery(name = "getSmallQuestionTags", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer(" +
                " t.tagId, t.name, t.color " +
                ") from Tags t left join t.questions tq where tq.questionId = :question"),
        @NamedQuery(name = "getUpdateEntityQuestion", query = "select q from Questions q " +
                " left join fetch q.questionContent " +
                " left join fetch q.tags t" +
                " where q.questionId = :id"),
        @NamedQuery(name = "removeQuestionTags", query = "delete from QuestionsTags where tagId in (:tags)"),
        @NamedQuery(name = "isQuestionVoted", query = "select questionVotersId from QuestionVoters where question = :question and user = :user")
})
public class Questions {

    private long questionId;
    private String title;
    private Date createDate;
    private long rating;
    private long views;
    private QuestionContent questionContent;
    private UserInfo userInfo;
    private Set<QuestionsTags> tags;
    private List<Answer> answers;
    private List<QuestionVoters> voters;
    private boolean activated = true;
    private long rightId = 0;

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
        this.createDate = new Date();
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "question",cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    public QuestionContent getQuestionContent() {
        return this.questionContent;
    }

    public void setQuestionContent(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @OneToMany(mappedBy = "questionId", cascade = CascadeType.ALL)
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

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "question")
    public List<QuestionVoters> getVoters() {
        return voters;
    }

    public void setVoters(List<QuestionVoters> voters) {
        this.voters = voters;
    }
}
