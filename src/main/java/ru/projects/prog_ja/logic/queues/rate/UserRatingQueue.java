package ru.projects.prog_ja.logic.queues.rate;

public interface UserRatingQueue {

    void putMessage(UserRateMessage userRateMessage);

}
