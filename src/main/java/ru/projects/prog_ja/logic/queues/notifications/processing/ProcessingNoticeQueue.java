package ru.projects.prog_ja.logic.queues.notifications.processing;

public interface ProcessingNoticeQueue<T> {

    boolean putMessage(T message);

}
