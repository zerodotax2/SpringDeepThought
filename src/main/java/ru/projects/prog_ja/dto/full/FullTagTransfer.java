package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonTagTransfer;

public class FullTagTransfer extends CommonTagTransfer {

    private long articles;
    private long questions;
    private long facts;
    private long users;
    private long problems;

    public FullTagTransfer(long id, String name, String description , String color,
                           long articles, long questions, long problems, long users, long facts) {
        super(id, name, description,color, facts, articles, questions);
        this.articles = articles;
        this.questions = questions;
        this.facts = facts;
        this.users = users;
        this.problems = problems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getArticles() {
        return articles;
    }

    public void setArticles(long posts) {
        this.articles = posts;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }

    public long getFacts() {
        return facts;
    }

    public void setFacts(long facts) {
        this.facts = facts;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getProblems() {
        return problems;
    }

    public void setProblems(long problems) {
        this.problems = problems;
    }
}
