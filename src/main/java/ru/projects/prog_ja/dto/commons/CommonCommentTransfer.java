package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonCommentTransfer implements Comparable<CommonCommentTransfer>{

    private long id;
    private String text;
    private SmallUserTransfer user;
    private Date createDate;
    private long rating;

    public CommonCommentTransfer(long id, String text, long rating, Date createDate, SmallUserTransfer user) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createDate = createDate;
        this.rating = rating;
    }

    public CommonCommentTransfer(long id, String text, long rating, Date createDate,
                                 long userId, String userLogin, String userImagePath, long userRating) {
        this.id = id;
        this.text = text;
        this.user = new SmallUserTransfer(userId, userLogin, userImagePath, userRating);
        this.createDate = createDate;
        this.rating = rating;
    }

    @Override
    public int compareTo(CommonCommentTransfer commonCommentTransfer){
       return  createDate.after(commonCommentTransfer.getCreateDate()) ? 1 : -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return text;
    }

    public void setComment(String text) {
        this.text = text;
    }

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
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
}
