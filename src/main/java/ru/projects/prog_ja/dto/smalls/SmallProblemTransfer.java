package ru.projects.prog_ja.dto.smalls;

import ru.projects.prog_ja.dto.TagsContainer;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;

import java.util.Date;


public class SmallProblemTransfer extends TagsContainer {

    protected String title;
    protected Date createDate;
    protected long rating;
    protected ProblemDifficult difficult;

    public SmallProblemTransfer(long id, String title, Date createDate, long rating, ProblemDifficult problemDifficult) {
        super(id);
        this.title = title;
        this.createDate = createDate;
        this.rating = rating;
        this.difficult = problemDifficult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public ProblemDifficult getDifficult() {
        return difficult;
    }

    public void setDifficult(ProblemDifficult difficult) {
        this.difficult = difficult;
    }
}
