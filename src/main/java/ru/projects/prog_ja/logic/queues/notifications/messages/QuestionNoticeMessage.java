package ru.projects.prog_ja.logic.queues.notifications.messages;

public class QuestionNoticeMessage {

    public enum Type{
        RATE, ANSWER, ANSWER_RATE, RIGHT
    }

    private final long entityID;
    private final long userID;
    private final Type type;

    public QuestionNoticeMessage(long entityID, long userID, Type type) {
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
