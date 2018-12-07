package ru.projects.prog_ja.logic.queues.rate;

public class UserRateMessage {

    private final RateMessageType type;
    private final long entityID;
    private final int rate;
    private final long userId;



    public UserRateMessage(RateMessageType type, long entityID, int rate, long userId) {
        this.type = type;
        this.entityID = entityID;
        this.rate = rate;
        this.userId = userId;
    }

    public RateMessageType getType() {
        return type;
    }

    public long getEntityID() {
        return entityID;
    }

    public int getRate() {
        return rate;
    }

    public long getUserId() {
        return userId;
    }
}
