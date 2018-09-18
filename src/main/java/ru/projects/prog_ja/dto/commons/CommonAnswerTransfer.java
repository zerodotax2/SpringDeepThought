package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonAnswerTransfer  implements Comparable<CommonAnswerTransfer>{

    private long id;
    private String htmlContent;
    private long rating;
    private boolean right;
    private Date createDate;
    private SmallUserTransfer user;

    public CommonAnswerTransfer(long id, String htmlContent, long rating, boolean right, Date createDate, SmallUserTransfer user) {
        this.id = id;
        this.htmlContent = htmlContent;
        this.rating = rating;
        this.right = right;
        this.createDate = createDate;
        this.user = user;
    }

    public CommonAnswerTransfer(long id, String htmlContent, long rating, boolean right, Date createDate, long userId, String login, String userImage, long userRating) {
        this.id = id;
        this.htmlContent = htmlContent;
        this.rating = rating;
        this.right = right;
        this.createDate = createDate;
        this.user = new SmallUserTransfer(userId, login, userImage, userRating);
    }

    @Override
    public int compareTo(CommonAnswerTransfer o) {

        return createDate.after(o.getCreateDate()) ? 1 : -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
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
}
