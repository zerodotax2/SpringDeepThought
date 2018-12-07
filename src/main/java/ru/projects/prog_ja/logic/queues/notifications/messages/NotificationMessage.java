package ru.projects.prog_ja.logic.queues.notifications.messages;

import ru.projects.prog_ja.dto.NoticeType;

public class NotificationMessage {

    private final long userId;
    private final String message;
    private final NoticeType type;

    public NotificationMessage(long userId, String message, NoticeType type) {
        this.userId = userId;
        this.message = message;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public NoticeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "NotificationMessage:\n" +
                "-userId: " + userId +"\n" +
                "-message: " + message + "\n" +
                "-type: " + type;
    }
}
