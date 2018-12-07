package ru.projects.prog_ja.logic.queues.notifications.services;

public interface ForumNoticeService {

    void accountCreateNotice(long userId);

    void answerForumQuestion(long questionId, long userId);

}
