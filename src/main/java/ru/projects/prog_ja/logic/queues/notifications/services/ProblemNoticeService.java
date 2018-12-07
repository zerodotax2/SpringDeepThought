package ru.projects.prog_ja.logic.queues.notifications.services;

public interface ProblemNoticeService {

    void rateNotice(long problemId, long userId);

    void commentNotice(long problemId, long userId);

    void commentRateNotice(long commentId, long userId);

    void feedbackNotice(long problemId, long userId);

}
