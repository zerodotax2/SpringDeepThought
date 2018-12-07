package ru.projects.prog_ja.logic.queues.notifications;

import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;

public interface NotificationQueue {

    void putMessage(NotificationMessage message);

}
