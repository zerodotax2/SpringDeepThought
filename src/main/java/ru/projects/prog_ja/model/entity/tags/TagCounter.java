package ru.projects.prog_ja.model.entity.tags;

import javax.persistence.*;

@Entity
@Table(name = "TagCounter")
public class TagCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_counter_id", unique = true, nullable = false)
    private long tagCounterId;

    @Column(name = "facts", length = 20)
    private long facts = 0;

    @Column(name = "articles", length = 20)
    private long articles = 0;

    @Column(name = "questions", length = 20)
    private long questions = 0;

    @Column(name = "problems", length = 20)
    private long problems = 0;

    @Column(name = "users", length = 20)
    private long users = 0;

    @Column(name = "uses", length = 20)
    private long uses = 0;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "tag_counter_fk"))
    private Tags tag;

    public TagCounter() {
    }

    public TagCounter(long facts, long articles, long questions, long problems, long users) {
        this.facts = facts;
        this.articles = articles;
        this.questions = questions;
        this.problems = problems;
        this.users = users;
    }

    public long getTagCounterId() {
        return tagCounterId;
    }

    public void setTagCounterId(long tagCounterId) {
        this.tagCounterId = tagCounterId;
    }

    public long getFacts() {
        return facts;
    }

    public void setFacts(long facts) {
        this.facts = facts;
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

    public long getProblems() {
        return problems;
    }

    public void setProblems(long problems) {
        this.problems = problems;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getUses() {
        return uses;
    }

    public void setUses(long uses) {
        this.uses = uses;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }
}
