package ru.projects.prog_ja.logic.queues.stats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.ProblemCounterMessage;
import ru.projects.prog_ja.logic.queues.stats.queues.ProblemStatsQueue;

@Service
@Scope("prototype")
public class ProblemCounterImpl implements ProblemCounter{

    private final ProblemStatsQueue queue;

    @Autowired
    public ProblemCounterImpl(ProblemStatsQueue queue) {
        this.queue = queue;
    }

    @Override
    public boolean incrementAttempts(long problemId, int count) {

        return queue.putMessage(new ProblemCounterMessage(problemId, count,
                ProblemCounterMessage.Type.ATTEMPTS));
    }

    @Override
    public boolean incrementSolved(long problemId, int count) {

        return queue.putMessage(new ProblemCounterMessage(problemId, count,
                ProblemCounterMessage.Type.SOLVED));
    }
}
