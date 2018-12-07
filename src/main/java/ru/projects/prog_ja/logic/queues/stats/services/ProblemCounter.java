package ru.projects.prog_ja.logic.queues.stats.services;

public interface ProblemCounter {

    boolean incrementAttempts(long problemId, int count);

    boolean incrementSolved(long problemId, int count);

}
