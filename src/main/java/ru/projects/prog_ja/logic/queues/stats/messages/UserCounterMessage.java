package ru.projects.prog_ja.logic.queues.stats.messages;

public class UserCounterMessage {

    public enum Type{

        ARTICLES, QUESTIONS, ANSWERS, PROBLEMS_SOLVED, PROBLEMS_CREATED,
        FACTS, TAGS, COMMENTS, NOTICE

    }

    private final long userId;
    private final int count;
    private final Type type;

    public UserCounterMessage(long userId, int count, Type type) {
        this.userId = userId;
        this.count = count;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public int getCount() {
        return count;
    }

    public Type getType() {
        return type;
    }
}
