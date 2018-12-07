package ru.projects.prog_ja.logic.queues.stats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.UserCounterMessage;
import ru.projects.prog_ja.logic.queues.stats.queues.UserStatsQueue;

@Service
@Scope("prototype")
public class UserCounterImpl implements UserCounter{
    
    private final UserStatsQueue queue;

    @Autowired
    public UserCounterImpl(UserStatsQueue queue) {
        this.queue = queue;
    }

    @Override
    public boolean incrementArticles(long userId, int count) {
        
        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.ARTICLES));
    }

    @Override
    public boolean incrementQuestions(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.QUESTIONS));
    }

    @Override
    public boolean incrementAnswers(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.ANSWERS));
    }

    @Override
    public boolean incrementProblemsSolved(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.PROBLEMS_SOLVED));
    }

    @Override
    public boolean incrementProblemsCreated(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.PROBLEMS_CREATED));
    }

    @Override
    public boolean incrementFacts(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.FACTS));
    }

    @Override
    public boolean incrementTags(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.TAGS));
    }

    @Override
    public boolean incrementComments(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.COMMENTS));
    }

    @Override
    public boolean incrementNotices(long userId, int count) {

        return queue.putMessage(new UserCounterMessage(userId, count,
                UserCounterMessage.Type.NOTICE));
    }
}
