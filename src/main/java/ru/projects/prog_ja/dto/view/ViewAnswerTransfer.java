package ru.projects.prog_ja.dto.view;

import java.util.Date;

public class ViewAnswerTransfer {

    private long id;
    private long question_id;
    private String question_title;
    private Date createDate;
    private long rating;
    private boolean right;

    public ViewAnswerTransfer(long id, long question_id, String question_title, Date createDate, long rating, boolean right) {
        this.id = id;
        this.question_id = question_id;
        this.question_title = question_title;
        this.createDate = createDate;
        this.rating = rating;
        this.right = right;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
