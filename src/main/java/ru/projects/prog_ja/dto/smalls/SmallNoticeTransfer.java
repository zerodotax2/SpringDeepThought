package ru.projects.prog_ja.dto.smalls;

import ru.projects.prog_ja.dto.NoticeType;

import java.util.Date;

public class SmallNoticeTransfer implements Comparable<SmallNoticeTransfer>{

    protected long id;

    protected String message;
    protected NoticeType type;
    protected Date createDate;

    public SmallNoticeTransfer(long id, String message, NoticeType type, Date createDate) {
        this.id = id;
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

    @Override
    public int compareTo(SmallNoticeTransfer o) {
        return this.createDate.after(o.createDate) ? -1 : 1;
    }
}
