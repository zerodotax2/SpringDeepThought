package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.smalls.SmallForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;

public class FullForumAnswer {


    private SmallUserQuestionTransfer question;
    private SmallForumAnswer answer;

    public FullForumAnswer() {
    }

    public FullForumAnswer(SmallUserQuestionTransfer question, SmallForumAnswer answer) {
        this.question = question;
        this.answer = answer;
    }

    public SmallUserQuestionTransfer getQuestion() {
        return question;
    }

    public void setQuestion(SmallUserQuestionTransfer question) {
        this.question = question;
    }

    public SmallForumAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(SmallForumAnswer answer) {
        this.answer = answer;
    }
}
