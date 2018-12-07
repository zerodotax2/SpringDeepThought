package ru.projects.prog_ja.logic.queues.stats.queues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.ProblemCounterMessage;
import ru.projects.prog_ja.model.dao.StatsDAO;

@Service
@Scope("singleton")
@EnableScheduling
public class ProblemStatsQueue extends AbstractStatsQueue<ProblemCounterMessage> {

    @Autowired
    public ProblemStatsQueue(StatsDAO statsDAO) {
        super(statsDAO);
    }

    @Override
    protected void send(ProblemCounterMessage message) throws Exception {

        switch (message.getType()){

            case SOLVED:
                statsDAO.addProblemSolved(message.getProblemId(), message.getCount());
                break;

            case ATTEMPTS:
                statsDAO.addProblemAttempts(message.getProblemId(), message.getCount());
                break;

        }

    }
}
