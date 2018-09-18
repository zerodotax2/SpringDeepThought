package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class FullUserTransfer extends CommonUserTransfer {

    private long articles;
    private long questions;
    private long comments;
    private long answers;
    private long problems;
    private long tags;
    private long facts;
    private long decided;
    private Set<SmallTagTransfer> interests;

    public FullUserTransfer(long id, String login, String middleImage, long rating, String firstName, String lastName, Date createDate, Date birthDate,
                            long articles, long comments, long questions,long answers,  long problems, long decided, long tags, long facts) {
        super(id, login, middleImage, rating,firstName, lastName, createDate, birthDate);
        this.articles = articles;
        this.comments = comments;
        this.questions = questions;
        this.answers = answers;
        this.tags = tags;
        this.interests = new HashSet<>();
        this.problems = problems;
        this.decided = decided;
        this.facts = facts;
    }

    public long getArticles() {
        return this.articles;
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

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getAnswers() {
        return answers;
    }

    public void setAnswers(long answers) {
        this.answers = answers;
    }

    public Set<SmallTagTransfer> getInterests() {
        return interests;
    }

    public void setInterests(Set<SmallTagTransfer> interests) {
        this.interests = interests;
    }

    public long getProblems() {
        return problems;
    }

    public void setProblems(long problems) {
        this.problems = problems;
    }

    public long getTags() {
        return tags;
    }

    public void setTags(long tags) {
        this.tags = tags;
    }

    public long getFacts() {
        return facts;
    }

    public void setFacts(long facts) {
        this.facts = facts;
    }

    public long getDecided() {
        return decided;
    }

    public void setDecided(long decided) {
        this.decided = decided;
    }
}
