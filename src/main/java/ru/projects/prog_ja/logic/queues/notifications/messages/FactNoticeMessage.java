package ru.projects.prog_ja.logic.queues.notifications.messages;

public class FactNoticeMessage {

    public enum Type {
        RATE
    }

    private final long id;
    private final long userId;
    private final Type type;

    public FactNoticeMessage(long id, long userId, Type type) {
        this.id = id;
        this.userId = userId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Type getType() {
        return type;
    }
}
