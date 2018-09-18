package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
        @org.hibernate.annotations.NamedQuery(name = "getFullProblem", query = "select p from Problem p " +
                " left join fetch p.content " +
                " left join fetch p.tags as t" +
                " left join fetch t.tagId " +
                " left join fetch p.creator " +
                " where p.problemId = :id"),
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
        @org.hibernate.annotations.NamedQuery(name = "updateProblemTitle", query = "update Problem set title = :title where problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemDifficult", query = "update Problem set difficult = :difficult where problemId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateProblemContent", query = "update ProblemContent pc set content = :content where problemContentId in (select problemContentId from Problem where problemId = :id)"),
        @org.hibernate.annotations.NamedQuery(name = "countProblems", query = "select distinct count(p.problemId) from Problem p"),
        @org.hibernate.annotations.NamedQuery(name = "getSmallProblemsByUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallProblemTransfer( " +
                " p.problemId, p.title, p.createDate, p.rating, p.difficult " +
                " ) from Problems p " +
                " where p.creator = :user")
})
public class Problem {

    public static final String GET_SMALL_PROBLEMS = "getSmallProblems";
    public static final String GET_FULL_PROBLEM = "getFullProblem";
    public static final String GET_PROBLEMS_BY_DIFFICULT = "getProblemsByDifficult";
    public static final String FIND_SMALL_PROBLEMS = "findSmallProblems";
    public static final String UPDATE_PROBLEM_TITLE = "updateProblemTitle";
    public static final String UPDATE_PROBLEM_DIFFICULT = "updateProblemDifficult";
    public static final String UPDATE_PROBLEM_CONTENT = "updateProblemContent";
    public static final String COUNT_PROBLEMS = "countProblems";
    public static final String GET_SMALL_PROBLEMS_BY_USER = "getSmallProblemsByUser";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", nullable = false, unique = true)
    private long problemId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_content_id", nullable = false)
    private ProblemContent content;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "problemId")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsTags> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "problemId")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<ProblemsUsers> users;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo creator;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Column(name = "rating", nullable = false)
    private long rating;

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
}
