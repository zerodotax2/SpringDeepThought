package ru.projects.prog_ja.logic.queues.notifications.messages;

public class ProblemNoticeMessage {

    public enum Type{
        RATE, COMMENT, COMMENT_RATE, FEEDBACK
    }

    private final long entityID;
    private final long userID;
    private final Type type;

    public ProblemNoticeMessage(long entityID, long userID, Type type) {
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
