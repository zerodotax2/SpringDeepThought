package ru.projects.prog_ja.logic.queues.stats.queues;

public interface StatsQueue<T> {

    boolean putMessage(T message);

}
