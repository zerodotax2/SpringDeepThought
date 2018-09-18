package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonCommentTransfer implements Comparable<CommonCommentTransfer>{

    private long postsCommentId;
    private String comment;
    private SmallUserTransfer user;
    private Date createDate;
    private long rating;

    public CommonCommentTransfer(long postsCommentId, String comment, long rating, Date createDate, SmallUserTransfer user) {
        this.postsCommentId = postsCommentId;
        this.comment = comment;
        this.user = user;
        this.createDate = createDate;
        this.rating = rating;
    }

    public CommonCommentTransfer(long postsCommentId, String comment, long rating, Date createDate,
                                 long userId, String userLogin, String userImagePath, long userRating) {
        this.postsCommentId = postsCommentId;
        this.comment = comment;
        this.user = new SmallUserTransfer(userId, userLogin, userImagePath, userRating);
        this.createDate = createDate;
        this.rating = rating;
    }

    @Override
    public int compareTo(CommonCommentTransfer commonCommentTransfer){
       return  createDate.after(commonCommentTransfer.getCreateDate()) ? 1 : -1;
    }

    public long getPostsCommentId() {
        return postsCommentId;
    }

    public void setPostsCommentId(long postsCommentId) {
        this.postsCommentId = postsCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
