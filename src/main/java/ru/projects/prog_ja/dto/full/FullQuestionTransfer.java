package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class FullQuestionTransfer extends CommonQuestionTransfer {

    private Set<CommonAnswerTransfer> answers;


    public FullQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long right,
                                long userId, String login, String smallImagePath,  long userRating,
                                String content) {
        super(id, title, createDate, rating, views, right, userId, smallImagePath, login, userRating, content);
        this.answers = new TreeSet<>();
    }

    public FullQuestionTransfer(long id, String title, Date createDate,  long rating, long views, long right,
                                SmallUserTransfer user, String content) {
        super(id, title, createDate,  rating, views, right, user, content);
        this.answers = new TreeSet<>();
    }

    public Set<CommonAnswerTransfer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<CommonAnswerTransfer> answers) {
        this.answers = answers;
    }

}
