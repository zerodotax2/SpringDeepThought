package ru.projects.prog_ja.logic.queues.notifications.messages;

public class ForumNotificationMessage {

    public enum Type{
        CREATE, FORUM_ANSWER
    }

    private final long entityID;
    private final long userID;
    private final Type type;

    public ForumNotificationMessage(long entityID, long userID, Type type) {
        this.entityID = entityID;
        this.userID = userID;
        this.type = type;
    }

    public long getEntityID() {
        return entityID;
    }

    public long getUserID() {
        return userID;
    }

    public Type getType() {
        return type;
    }
}
