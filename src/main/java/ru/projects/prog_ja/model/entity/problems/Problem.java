package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "Problem")
@NamedQueries({
        @NamedQuery(name = "getSmallProblems", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult " +
                " ) from Problem p " +
                " left join p.counter pc" +
                " order by p.createDate desc"),
        @NamedQuery(name = "getFullProblem", query = "select new ru.projects.prog_ja.dto.full.FullProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult, c.htmlContent, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from Problem p " +
                " left join p.content c" +
                " left join p.creator u  " +
                " left join p.counter pc" +
                " where p.problemId = :id  "),
        @NamedQuery(name = "getEditProblem", query = "select new ru.projects.prog_ja.dto.view.EditProblemDTO( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult, c.htmlContent, u.userId, u.login, u.smallImagePath, u.rating, s.solution, s.answer " +
                " ) from Problem p " +
                " left join p.content c" +
                " left join p.creator u  " +
                " left join p.counter pc " +
                " left join p.problemSolution s" +
                " where p.problemId = :id  "),
        @NamedQuery(name = "getProblemSolution", query = "select new ru.projects.prog_ja.dto.full.FullSolutionTransfer( " +
                " ps.problemSolutionId, p.problemId, p.title, ps.solution, ps.answer " +
                " ) from Problem p " +
                " left join p.problemSolution ps " +
                " where p.problemId = :id  "),
        @NamedQuery(name = "getProblemsByDifficult", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult " +
                " ) from Problem p " +
                " left join p.counter pc " +
                " where p.difficult = :difficult " +
                " order by p.createDate desc"),
        @NamedQuery(name = "findSmallProblems", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult " +
                " ) from Problem p " +
                " left join p.counter pc "+
                " where lower(p.title) like lower(:search) " +
                " order by p.createDate desc"),
        @NamedQuery(name = "getTagsByProblem", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer( " +
                " t.tagId, t.name, t.color " +
                " ) from Tags t " +
                " left join t.problems pt " +
                " left join pt.problemId p " +
                " where p.problemId = :id "),
        @NamedQuery(name = "updateProblemTitle", query = "update Problem set title = :title where problemId = :id "),
        @NamedQuery(name = "updateProblemRate", query = "update Problem set rating = rating + :rate where problemId = :id and creator != :user"),
        @NamedQuery(name = "updateProblemComment", query = "update ProblemComment set comment = :comment where problemCommentId = :id and userInfo = :user"),
        @NamedQuery(name = "removeProblemComment", query = "delete from ProblemComment where problemCommentId = :id and userInfo = :user"),
        @NamedQuery(name = "updateProblemCommentRate", query = "update ProblemComment set rating = rating + :rate where problemCommentId = :id and creator != :user"),
        @NamedQuery(name = "updateProblemDifficult", query = "update Problem set difficult = :difficult where problemId = :id"),
        @NamedQuery(name = "updateProblemAnswer", query = "update ProblemSolution set answer = :answer where problem = :problem"),
        @NamedQuery(name = "updateProblemSolution", query = "update ProblemSolution set solution = :solution where problem = :problem"),
        @NamedQuery(name = "updateProblemContent", query = "update ProblemContent pc set pc.htmlContent = :content where problem = :problem"),
        @NamedQuery(name = "countProblems", query = "select distinct count(p.problemId) from Problem p"),
        @NamedQuery(name = "deleteProblem", query = "delete from Problem where problemId = :id and creator = :user"),
        @NamedQuery(name = "getSmallProblemsByUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, pc.solved, p.difficult " +
                " ) from Problem p " +
                " left join p.counter pc " +
                " where p.creator = :user"),
        @NamedQuery(name = "getProblemComment", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer( " +
                " pc.problemCommentId, pc.comment, pc.rating, pc.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from ProblemComment pc " +
                " left join pc.creator u " +
                " where pc.problemCommentId = :id "),
        @NamedQuery(name = "getCommentsByProblem", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer( " +
                " pc.problemCommentId, pc.comment, pc.rating, pc.createDate, u.userId, u.login, u.smallImagePath, u.rating  " +
                " ) from ProblemComment pc" +
                " left join pc.creator u  " +
                " where pc.problem = :problem "),
        @NamedQuery(name = "getProblemAnswer", query = "select ps.answer from Problem p " +
                "left join p.problemSolution ps where p.problemId = :id"),
        @NamedQuery(name = "getProblemFeedback", query = "select new ru.projects.prog_ja.dto.smalls.SmallFeedbackDTO(" +
                " pf.problemFeedbackId, pf.text, pf.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from ProblemFeedback pf " +
                " left join pf.userInfo u " +
                " left join pf.problem p  " +
                " where p.problemId = :id " +
                " order by pf.createDate desc"),
        @NamedQuery(name = "getProblemTitle", query = "select title from Problem where problemId = :id"),
        @NamedQuery(name = "updateProblemOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :problem in elements(problems) and userId != :user"),
        @NamedQuery(name = "updateProblemCommentOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :comment in elements(userProblemComments) and userId != :user"),
        @NamedQuery(name = "updateProblemView", query = "update Problem set views = views + :view where problemId = :id"),
        @NamedQuery(name = "getProblemNoticeTemplate", query =  "select new ru.projects.prog_ja.dto.NoticeEntityTemplateDTO (" +
                " u.userId, p.title " +
                " ) from Problem p  left join p.creator u where p.problemId = :id"),
        @NamedQuery(name = "getProblemNoticeCommentTemplate", query =  "select new ru.projects.prog_ja.dto.NoticeCommentTemplateDTO (" +
                " p.problemId, u.userId, p.title " +
                " ) from ProblemComment pc left join pc.problem p left join pc.creator u where pc.problemCommentId = :id"),
        @NamedQuery(name = "getTagsIDSByProblem", query = "select t.tagId from Problem p left join p.tags pt left join pt.tagId t where p.problemId = :id"),
        @NamedQuery(name = "getUpdateProblemEntity", query = "select p from Problem p " +
                " left join fetch p.tags " +
                " left join fetch p.content" +
                " left join fetch p.problemSolution " +
                " where p.problemId = :id"),
        @NamedQuery(name = "removeProblemTags", query = "delete from ProblemsTags where tagId in (:tags)"),
        @NamedQuery(name = "isProblemVoted", query = "select problemVotersId from ProblemVoters where problem = :problem and user = :user"),
        @NamedQuery(name = "isCommentProblemVoted", query = "select problemCommentVotersId from ProblemCommentVoters where comment = :comment and user = :user"),
        @NamedQuery(name = "isAlreadyDecided", query = "select problemsSolvedUsersId from ProblemsSolvedUsers where problem = :problem and user = :user")
})
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", nullable = false, unique = true)
    private long problemId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "problem", optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private ProblemContent content;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "problem", optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private ProblemSolution problemSolution;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "problemId", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsTags> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsSolvedUsers> solvers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemComment> comments = new TreeSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemFeedback> feedbacks;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "problem")
    private List<ProblemVoters> voters;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "problem", optional = false, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProblemCounter counter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo creator;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Column(name = "rating", nullable = false)
    private long rating;

    @Column(name = "views", nullable = false)
    private long views;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficult", nullable = false)
    private ProblemDifficult difficult;

    public Problem() {
    }

    public Problem(String title, ProblemDifficult difficult) {
        this.title = title;
        this.difficult = difficult;
        this.createDate = new Date();
        activated = false;
        rating = 0L;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProblemContent getContent() {
        return content;
    }

    public void setContent(ProblemContent content) {
        this.content = content;
    }

    public Set<ProblemsTags> getTags() {
        return tags;
    }

    public void setTags(Set<ProblemsTags> tags) {
        this.tags = tags;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public UserInfo getCreator() {
        return creator;
    }

    public void setCreator(UserInfo creator) {
        this.creator = creator;
    }

    public ProblemDifficult getDifficult() {
        return difficult;
    }

    public void setDifficult(ProblemDifficult difficult) {
        this.difficult = difficult;
    }

    public ProblemSolution getProblemSolution() {
        return problemSolution;
    }

    public void setProblemSolution(ProblemSolution problemSolution) {
        this.problemSolution = problemSolution;
    }

    public Set<ProblemComment> getComments() {
        return comments;
    }

    public void setComments(Set<ProblemComment> comments) {
        this.comments = comments;
    }

    public Set<ProblemsSolvedUsers> getSolvers() {
        return solvers;
    }

    public void setSolvers(Set<ProblemsSolvedUsers> solvers) {
        this.solvers = solvers;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public Set<ProblemFeedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<ProblemFeedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public ProblemCounter getCounter() {
        return counter;
    }

    public void setCounter(ProblemCounter counter) {
        this.counter = counter;
    }

    public List<ProblemVoters> getVoters() {
        return voters;
    }

    public void setVoters(List<ProblemVoters> voters) {
        this.voters = voters;
    }
}
