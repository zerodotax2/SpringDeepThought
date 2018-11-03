package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "Problem")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getSmallProblems", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, p.difficult " +
                " ) from Problems p" +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "getFullProblem", query = "select new ru.projects.prog_ja.dto.full.FullProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, count(users.problemsUsersId), p.difficult, p.htmlContent " +
                " ) from Problem p " +
                " left join p.content " +
                " left join p.users u " +
                " where p.problemId = :id  " +
                " group by u.problemsUsersId"),
        @org.hibernate.annotations.NamedQuery(name = "getProblemSolution", query = "select new ru.projects.prog_ja.dto.full.FullSolutionTransfer( " +
                " ps.problemSolutionId, ps.solution, ps.answer " +
                " ) from Problem p " +
                " left join p.problemSolution ps " +
                " where p.problemId = :id  "),
        @org.hibernate.annotations.NamedQuery(name = "getProblemsByDifficult", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, p.difficult " +
                " ) from Problems p " +
                " where p.difficult = :difficult " +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "findSmallProblems", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, p.difficult " +
                " ) from Problems p " +
                " where lower(p.title) like lower(:search) " +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "getTagsByProblem", query = "select new ru.projects.prog_ja.dto.SmallTagTransfer( " +
                " t.tagId, t.name, t.color " +
                " ) from Tags t " +
                " left join t.problems p " +
                " where p.problemId = :id "),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemTitle", query = "update Problem set title = :title where problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemRate", query = "update Problem set rating = rating + :rate where problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemComment", query = "update ProblemComment set comment = :comment where problemCommentId = :id and userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "removeProblemComment", query = "delete from ProblemComment where problemCommentId = :id and userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemCommentRate", query = "update ProblemComment set rating = rating + :rate where problemCommentId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemDifficult", query = "update Problem set difficult = :difficult where problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemAnswer", query = "update ProblemSolution ps set answer = :answer where problemSolutionId in (select problemSolutionId from Problem where problemId = :id)"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemSolution", query = "update ProblemSolution ps set solution = :solution where problemSolutionId in (select problemSolutionId from Problem where problemId = :id)"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemContent", query = "update ProblemContent pc set content = :content where problemContentId in (select problemContentId from Problem where problemId = :id)"),
        @org.hibernate.annotations.NamedQuery(name = "countProblems", query = "select distinct count(p.problemId) from Problem p"),
        @org.hibernate.annotations.NamedQuery(name = "deleteProblem", query = "delete from Problems where problemId = :id and creator = :user"),
        @org.hibernate.annotations.NamedQuery(name = "getSmallProblemsByUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, p.difficult " +
                " ) from Problems p " +
                " where p.creator = :user"),
        @org.hibernate.annotations.NamedQuery(name = "getComment", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer( " +
                " pc.problemCommentId, pc.comment, pc.rating, pc.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from ProblemComment pc " +
                " left join pc.creator u " +
                " where pc.problemCommentId = :id "),
        @org.hibernate.annotations.NamedQuery(name = "getCommentsByProblem", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer( " +
                " pc.problemCommentId, pc.comment, pc.rating, pc.createDate, u.userId, u.login, u.smallImagePath, u.rating  " +
                " ) from Problem p " +
                " left join p.comments pc " +
                " left join pc.creator u  " +
                " where p.problemId = :id "),
        @org.hibernate.annotations.NamedQuery(name = "getProblemAnswer", query = "select ps.answer from Problem p " +
                "left join p.problemSolution ps where p.problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "getProblemFeedback", query = "select new ru.projects.prog_ja.dto.smalls.SmallFeedbackDTO(" +
                " pf.problemFeedbackId, pf.text, pf.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from ProblemFeedback pf " +
                " left join pf.userInfo u " +
                " left join pf.problem p  " +
                " where p.problemId = :id " +
                " order by pf.createDate desc")
})
public class Problem {

    public static final String GET_SMALL_PROBLEMS = "getSmallProblems";
    public static final String GET_TAGS_BY_PROBLEMS = "getTagsByProblem";
    public static final String GET_FULL_PROBLEM = "getFullProblem";
    public static final String GET_PROBLEM_SOLUTION = "getProblemSolution";
    public static final String GET_PROBLEMS_BY_DIFFICULT = "getProblemsByDifficult";
    public static final String FIND_SMALL_PROBLEMS = "findSmallProblems";
    public static final String UPDATE_PROBLEM_TITLE = "updateProblemTitle";
    public static final String UPDATE_PROBLEM_DIFFICULT = "updateProblemDifficult";
    public static final String UPDATE_PROBLEM_CONTENT = "updateProblemContent";
    public static final String UPDATE_PROBLEM_SOLUTION = "updateProblemSolution";
    public static final String UPDATE_PROBLEM_ANSWER = "updateProblemAnswer";
    public static final String UPDATE_PROBLEM_COMMENT_RATE = "updateProblemCommentRate";
    public static final String UPDATE_PROBLEM_RATE = "updateProblemRate";
    public static final String REMOVE_PROBLEM_COMMENT = "removeProblemComment";
    public static final String UPDATE_PROBLEM_COMMENT = "updateProblemComment";
    public static final String COUNT_PROBLEMS = "countProblems";
    public static final String DELETE_PROBLEM = "deleteProblem";
    public static final String GET_COMMENT = "getComment";
    public static final String GET_SMALL_PROBLEMS_BY_USER = "getSmallProblemsByUser";
    public static final String GET_COMMENTS_BY_PROBLEM = "getCommentsByProblem";
    public static final String GET_PROBLEM_ANSWER = "getProblemAnswer";
    public static final String GET_PROBLEM_FEEDBACK = "getProblemFeedback";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", nullable = false, unique = true)
    private long problemId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_content_id", nullable = false, foreignKey = @ForeignKey(name = "problem_content_fk"))
    private ProblemContent content;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_solution_id", nullable = false, foreignKey = @ForeignKey(name = "problem_solution_fk"))
    private ProblemSolution problemSolution;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "problemId")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsTags> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "problemId")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsUsers> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsSolvedUsers> solvers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemComment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "problem")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemFeedback> feedbacks;

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
        this.createDate = new Date(new java.util.Date().getTime());
        activated = false;
        rating = 0;
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

    public Set<ProblemsUsers> getUsers() {
        return users;
    }

    public void setUsers(Set<ProblemsUsers> users) {
        this.users = users;
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
}
