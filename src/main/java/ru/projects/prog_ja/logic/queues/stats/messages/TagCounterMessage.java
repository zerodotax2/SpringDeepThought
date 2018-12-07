package ru.projects.prog_ja.logic.queues.stats.messages;

public class TagCounterMessage {

    public enum Type{

        ARTICLES, QUESTIONS, PROBLEMS, FACTS, USERS

    }

    private final long tagId;
    private final int count;
    private final Type type;

    public TagCounterMessage(long tagId, int count, Type type) {
        this.tagId = tagId;
        this.count = count;
        this.type = type;
    }

    public long getTagId() {
        return tagId;
    }

    public int getCount() {
        return count;
    }

    public Type getType() {
        return type;
    }
}
