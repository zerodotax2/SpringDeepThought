package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.ProblemDifficult;

import java.util.Date;

public class FullProblemTransfer extends SmallProblemTransfer {

    private String htmlContent;
    private SmallUserTransfer user;

    public FullProblemTransfer(long id, String title, Date createDate, long rating, long decided, ProblemDifficult problemDifficult, String htmlContent,
                               SmallUserTransfer user) {
        super(id, title, createDate, rating, decided,problemDifficult );
        this.htmlContent = htmlContent;
        this.user = user;
    }
    public FullProblemTransfer(long id, String title, Date createDate, long rating, long decided, ProblemDifficult problemDifficult, String htmlContent,
                               long userId, String login, String smallImagePath, long userRating) {
        super(id, title, createDate, rating, decided,problemDifficult );
        this.htmlContent = htmlContent;
        this.user = new SmallUserTransfer(userId, login, smallImagePath, userRating);
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }
}
