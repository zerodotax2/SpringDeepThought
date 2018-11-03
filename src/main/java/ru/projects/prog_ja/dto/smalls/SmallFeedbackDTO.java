package ru.projects.prog_ja.dto.smalls;

import java.util.Date;

public class SmallFeedbackDTO {

    private long id;
    private String text;
    private Date createDate;
    private SmallUserTransfer user;

    public SmallFeedbackDTO() {
    }

    public SmallFeedbackDTO(long id, String text, Date createDate, SmallUserTransfer user) {
        this.id = id;
        this.text = text;
        this.createDate = createDate;
        this.user = user;
    }

    public SmallFeedbackDTO(long id, String text, Date createDate,
                            long userId, String login, String userImage, long rating) {
        this.id = id;
        this.text = text;
        this.createDate = createDate;
        this.user = new SmallUserTransfer(userId, login, userImage, rating);
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }
}
