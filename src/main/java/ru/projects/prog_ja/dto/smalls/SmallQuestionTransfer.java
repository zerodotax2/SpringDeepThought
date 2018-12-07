package ru.projects.prog_ja.dto.smalls;

import ru.projects.prog_ja.dto.TagsContainer;

import javax.json.bind.annotation.JsonbDateFormat;
import java.util.Date;

public class SmallQuestionTransfer  extends TagsContainer {

    @JsonbDateFormat(value = "dd-MM-yy в HH:mm")
    protected Date createDate;
    protected String title;
    protected long rating;
    protected SmallUserTransfer user;
    protected long right;
    protected long views;

    public SmallQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long right, long userId, String login, String smallImagePath,  long userRating) {
        super(id);
        this.createDate = createDate;
        this.title = title;
        this.user = new SmallUserTransfer(userId, login, smallImagePath, userRating);
        this.rating = rating;
        this.views = views;
        this.right = right;
    }

    public SmallQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long right, SmallUserTransfer user) {
        super(id);
        this.createDate = createDate;
        this.title = title;
        this.rating = rating;
        this.user = user;
        this.views = views;
        this.right = right;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }
}
