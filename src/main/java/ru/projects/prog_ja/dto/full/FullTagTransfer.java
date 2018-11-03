package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class FullTagTransfer extends CommonTagTransfer {

    private long facts;
    private long users;
    private Date createDate;
    private SmallUserTransfer user;

    public FullTagTransfer(long id, String name, String description , String color,
                           long articles, long questions, long problems, long users, long facts,
                           Date createDate,
                           SmallUserTransfer user) {
        super(id, name, description,color, articles, questions, problems);
        this.facts = facts;
        this.users = users;
        this.createDate = createDate;
        this.user = user;
    }

    public FullTagTransfer(long id, String name, String description , String color,
                           long articles, long questions, long problems, long users, long facts,
                           Date createDate,
                           long userId, String login, String userImage, long rating) {
        super(id, name, description,color, articles, questions, problems);
        this.facts = facts;
        this.users = users;
        this.createDate = createDate;
        this.user = new SmallUserTransfer(userId, login, userImage, rating);
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

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
