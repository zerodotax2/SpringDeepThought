package ru.projects.prog_ja.logic.queues.notifications.services;

public interface ArticleNoticeService {

    void addRateNotice(long articleId, long userId);

    void addCommentNotice(long articleId, long userId);

    void addCommentRateNotice(long commentId, long userId);

}
