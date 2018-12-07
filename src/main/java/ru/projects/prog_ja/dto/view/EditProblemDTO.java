package ru.projects.prog_ja.dto.view;

import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class EditProblemDTO extends FullProblemTransfer {

    private String solution;
    private String answer;

    public EditProblemDTO(long id, String title, Date createDate, long rating, long decided, ProblemDifficult problemDifficult, String htmlContent, SmallUserTransfer user, String solution, String answer) {
        super(id, title, createDate, rating, decided, problemDifficult, htmlContent, user);
        this.solution = solution;
        this.answer = answer;
    }

    public EditProblemDTO(long id, String title, Date createDate, long rating, long decided, ProblemDifficult problemDifficult, String htmlContent, long userId, String login, String smallImagePath, long userRating, String solution, String answer) {
        super(id, title, createDate, rating, decided, problemDifficult, htmlContent, userId, login, smallImagePath, userRating);
        this.solution = solution;
        this.answer = answer;
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
}
