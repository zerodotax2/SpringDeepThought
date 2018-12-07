package ru.projects.prog_ja.logic.queues.notifications.services;

public interface QuestionNoticeService {

    void rateNotice(long questionId, long userId);

    void answerNotice(long questionId, long userId);

    void rightAnswerNotice(long answerId);

    void answerRateNotice(long answerId, long userId);

}
