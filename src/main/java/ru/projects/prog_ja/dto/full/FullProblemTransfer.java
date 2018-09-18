package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;

import java.util.Date;

public class FullProblemTransfer extends SmallProblemTransfer {

    private String htmlContent;

    public FullProblemTransfer(long id, String title, Date createDate, long rating, ProblemDifficult problemDifficult, String htmlContent) {
        super(id, title, createDate, rating, problemDifficult );
        this.htmlContent = htmlContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
