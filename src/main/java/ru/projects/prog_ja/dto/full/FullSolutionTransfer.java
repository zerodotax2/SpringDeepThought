package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;

import java.util.Set;
import java.util.TreeSet;

public class FullSolutionTransfer {

    private long id;
    private long problemId;
    private String problemTitle;
    private String solution;
    private String answer;
    private Set<CommonCommentTransfer> comments;

    public FullSolutionTransfer(long id, long problemId, String problemTitle, String solution, String answer) {
        this.id = id;
        this.solution = solution;
        this.answer = answer;
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        comments = new TreeSet<>();
    }

    public long getId() {
        return id;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Set<CommonCommentTransfer> getComments() {
        return comments;
    }

    public void setComments(Set<CommonCommentTransfer> comments) {
        this.comments = comments;
    }
}
