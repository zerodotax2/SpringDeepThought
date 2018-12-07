package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonQuestionTransfer extends SmallQuestionTransfer {

    protected String content;

    public CommonQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long right, long userId, String login, String smallImagePath,  long userRating,
                                  String content) {
        super(id, title, createDate,  rating, views, right, userId, smallImagePath, login, userRating);
        this.content = content;
    }

    public CommonQuestionTransfer(long id,  String title, Date createDate,  long rating, long views, long right, SmallUserTransfer user, String content) {
        super(id, title, createDate,  rating, views,right, user);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
