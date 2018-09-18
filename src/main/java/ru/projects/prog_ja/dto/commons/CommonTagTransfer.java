package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

public class CommonTagTransfer extends SmallTagTransfer {

    protected String description;

    protected long articles;
    protected long questions;
    protected long problems;

    public CommonTagTransfer(long id, String name,String color, String description, long articles, long questions, long problems) {
        super(id, name, color);
        this.description = description;
        this.articles = articles;
        this.questions = questions;
        this.problems = problems;
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
}
