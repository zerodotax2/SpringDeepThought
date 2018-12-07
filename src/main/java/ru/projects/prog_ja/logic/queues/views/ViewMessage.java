package ru.projects.prog_ja.logic.queues.views;

public class ViewMessage {

    private long id;
    public int views = 1;
    private long userId;

    public ViewMessage(long id,long userId) {
        this.id = id;
        this.userId = userId;
    }

    public ViewMessage increment(){
        views++;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
