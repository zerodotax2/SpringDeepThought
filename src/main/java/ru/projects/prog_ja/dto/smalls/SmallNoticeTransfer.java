package ru.projects.prog_ja.dto.smalls;

import ru.projects.prog_ja.model.entity.user.NoticeType;

import java.util.Date;

public class SmallNoticeTransfer {

    protected long id;

    protected String title;
    protected String message;
    protected NoticeType type;
    protected Date createDate;

    public SmallNoticeTransfer(long id, String title, String message, NoticeType type, Date createDate) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NoticeType getType() {
        return type;
    }

    public void setType(NoticeType type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
