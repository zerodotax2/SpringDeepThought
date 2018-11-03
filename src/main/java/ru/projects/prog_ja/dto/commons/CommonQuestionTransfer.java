package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonQuestionTransfer extends SmallQuestionTransfer {

    protected String content;

    public CommonQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long userId, String smallImagePath, String login, long userRating,
                                  String content) {
        super(id, title, createDate,  rating, views, userId, smallImagePath, login, userRating);
        this.content = content;
    }

    public CommonQuestionTransfer(long id,  String title, Date createDate,  long rating, long views, SmallUserTransfer user, String content) {
        super(id, title, createDate,  rating, views, user);
        this.content = content;
    }

    public String getSubContent() {
        return content;
    }

    public void setSubContent(String content) {
        this.content = content;
    }
}
