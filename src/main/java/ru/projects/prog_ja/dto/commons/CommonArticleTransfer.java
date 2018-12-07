package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;

import java.util.Date;

public class CommonArticleTransfer extends SmallArticleTransfer {

    protected Date createDate;
    protected long views;

    public CommonArticleTransfer(long id, String title, String middleImage, Date createDate, long views, long rating){
        super(id, title, middleImage, rating);
        this.createDate = createDate;
        this.views = views;
    }

    @Override
    public String toString() {
        return "\nЗаголовок:" + title + "\n Теги: \n" + tags;

    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

}
