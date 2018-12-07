package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;

@Entity
@Table(name = "UserCounter")
@NamedQueries({
        @NamedQuery(name = "incrementProblemAttempts", query = "update ProblemCounter set attempts = attempts + :num " +
                " where problem = :problem"),
        @NamedQuery(name = "incrementProblemSolved", query = "update ProblemCounter set solved = solved + :num " +
                " where problem = :problem"),
        @NamedQuery(name = "incrementTagArticles", query = "update TagCounter set articles = articles + :num, uses = uses + :num" +
                " where tag = :tag"),
        @NamedQuery(name = "incrementTagQuestions", query = "update TagCounter set questions = questions + :num, uses = uses + :num " +
                " where tag = :tag"),
        @NamedQuery(name = "incrementTagProblems", query = "update TagCounter set problems = problems + :num, uses = uses + :num " +
                " where tag = :tag"),
        @NamedQuery(name = "incrementTagFacts", query = "update TagCounter set facts = facts + :num, uses = uses + :num " +
                " where tag = :tag"),
        @NamedQuery(name = "incrementTagUsers", query = "update TagCounter set users = users + :num, uses = uses + :num " +
                " where tag = :tag"),
        @NamedQuery(name = "incrementUserArticles", query = "update UserCounter set articles = articles + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserQuestions", query = "update UserCounter set questions = questions + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserAnswers", query = "update UserCounter set answers = answers + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserProblemsSolved", query = "update UserCounter set decided = decided + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserProblemsCreated", query = "update UserCounter set problems = problems + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserFacts", query = "update UserCounter set facts = facts + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserTags", query = "update UserCounter set tags = tags + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserComments", query = "update UserCounter set comments = comments + :num " +
                " where user = :user"),
        @NamedQuery(name = "incrementUserNotices", query = "update UserCounter set notices = notices + :num " +
                " where user = :user"),
})
public class UserCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_counter_id", unique = true, nullable = false, length = 32)
    private long userCounterId;

    @Column(name = "articles")
    private long articles = 0;

    @Column(name = "questions")
    private long questions;

    @Column(name = "answers")
    private long answers;

    @Column(name = "problems")
    private long problems;

    @Column(name = "decided")
    private long decided;

    @Column(name = "comments")
    private long comments;

    @Column(name = "tags")
    private long tags;

    @Column(name = "facts")
    private long facts;

    @Column(name = "notices")
    private long notices;

    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_counter_fk"))
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserInfo user;

    public UserCounter() {
    }

    public UserCounter(long articles, long comments, long questions, long answers, long problems, long decided, long tags, long facts, long notices) {
        this.articles = articles;
        this.comments = comments;
        this.questions = questions;
        this.answers = answers;
        this.problems = problems;
        this.decided = decided;
        this.tags = tags;
        this.facts = facts;
        this.notices = notices;
    }

    public long getUserCounterId() {
        return userCounterId;
    }

    public void setUserCounterId(long userCounterId) {
        this.userCounterId = userCounterId;
    }

    public long getArticles() {
        return articles;
    }

    public void setArticles(long articles) {
        this.articles = articles;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }

    public long getAnswers() {
        return answers;
    }

    public void setAnswers(long answers) {
        this.answers = answers;
    }

    public long getProblems() {
        return problems;
    }

    public void setProblems(long problems) {
        this.problems = problems;
    }

    public long getDecided() {
        return decided;
    }

    public void setDecided(long decided) {
        this.decided = decided;
    }

    public long getTags() {
        return tags;
    }

    public void setTags(long tags) {
        this.tags = tags;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getFacts() {
        return facts;
    }

    public void setFacts(long facts) {
        this.facts = facts;
    }

    public long getNotices() {
        return notices;
    }

    public void setNotices(long notices) {
        this.notices = notices;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
