package ru.projects.prog_ja.dto.smalls;

import java.util.Date;

public class SmallUserQuestionTransfer {

    private long id;
    private SmallUserTransfer user;
    private String text;
    private Date createDate;
    private boolean answered;

    public SmallUserQuestionTransfer() {
    }

    public SmallUserQuestionTransfer(long id,  String text, Date createDate, SmallUserTransfer user) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.createDate = createDate;
    }

    public SmallUserQuestionTransfer(long id, String text, Date createDate,
                                     long userId, String login, String userImage, long userRating) {
        this.id = id;
        this.text = text;
        this.createDate = createDate;
        this.user = new SmallUserTransfer(userId, login, userImage, userRating);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
