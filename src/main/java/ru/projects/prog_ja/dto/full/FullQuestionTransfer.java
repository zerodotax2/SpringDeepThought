package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class FullQuestionTransfer extends CommonQuestionTransfer {

    private Set<CommonAnswerTransfer> answers;
    private long right;

    public FullQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long userId, String smallImagePath, String login, long userRating, String content, long right) {
        super(id, title, createDate, rating, views, userId, smallImagePath, login, userRating, content);
        this.answers = new TreeSet<>();
        this.right = right;
    }

    public FullQuestionTransfer(long id, String title, Date createDate,  long rating, long views, SmallUserTransfer user, String content, long right) {
        super(id, title, createDate,  rating, views, user, content);
        this.answers = new TreeSet<>();
        this.right = right;
    }

    public Set<CommonAnswerTransfer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<CommonAnswerTransfer> answers) {
        this.answers = answers;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }
}
