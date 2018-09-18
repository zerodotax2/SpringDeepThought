package ru.projects.prog_ja.model.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "UserCounter")
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

    public UserCounter() {
    }

    public UserCounter(long articles, long comments, long questions, long answers, long problems, long decided, long tags, long facts) {
        this.articles = articles;
        this.comments = comments;
        this.questions = questions;
        this.answers = answers;
        this.problems = problems;
        this.decided = decided;
        this.tags = tags;
        this.facts = facts;
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
}
