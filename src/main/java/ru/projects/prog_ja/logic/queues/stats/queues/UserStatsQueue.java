package ru.projects.prog_ja.logic.queues.stats.queues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.UserCounterMessage;
import ru.projects.prog_ja.model.dao.StatsDAO;

@Service
@Scope("singleton")
@EnableScheduling
public class UserStatsQueue extends AbstractStatsQueue<UserCounterMessage> {

    @Autowired
    public UserStatsQueue(StatsDAO statsDAO) {
        super(statsDAO);
    }

    @Override
    protected void send(UserCounterMessage message) throws Exception {

        switch (message.getType()){

            case ARTICLES:
                statsDAO.addUserArticles(message.getUserId(), message.getCount());
                break;

            case QUESTIONS:
                statsDAO.addUserQuestions(message.getUserId(), message.getCount());
                break;

            case ANSWERS:
                statsDAO.addUserAnswers(message.getUserId(), message.getCount());
                break;

            case PROBLEMS_CREATED:
                statsDAO.addUserProblemsCreated(message.getUserId(), message.getCount());
                break;

            case PROBLEMS_SOLVED:
                statsDAO.addUserProblemsSolved(message.getUserId(), message.getCount());
                break;

            case FACTS:
                statsDAO.addUserFacts(message.getUserId(), message.getCount());
                break;

            case TAGS:
                statsDAO.addUserTags(message.getUserId(), message.getCount());
                break;

            case COMMENTS:
                statsDAO.addUserComments(message.getUserId(), message.getCount());
                break;
            case NOTICE:
                statsDAO.addUserNotice(message.getUserId(), message.getCount());
        }

    }
}
