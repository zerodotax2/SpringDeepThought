package ru.projects.prog_ja.logic.queues.stats.messages;

public class ProblemCounterMessage {

    public enum Type{

        ATTEMPTS, SOLVED

    }

    private final long problemId;
    private final int count;
    private final Type type;

    public ProblemCounterMessage(long problemId, int count, Type type) {
        this.problemId = problemId;
        this.count = count;
        this.type = type;
    }

    public long getProblemId() {
        return problemId;
    }

    public int getCount() {
        return count;
    }

    public Type getType() {
        return type;
    }
}
