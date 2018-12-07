package ru.projects.prog_ja.logic.queues.stats.queues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.TagCounterMessage;
import ru.projects.prog_ja.model.dao.StatsDAO;

@Service
@Scope(value = "singleton")
@EnableScheduling
public class TagStatsQueue extends AbstractStatsQueue<TagCounterMessage> {

    @Autowired
    public TagStatsQueue(StatsDAO statsDAO) {
        super(statsDAO);
    }


    @Override
    protected void send(TagCounterMessage message) throws Exception{

        switch (message.getType()){

            case FACTS:

                statsDAO.addTagFacts(message.getTagId(), message.getCount());
                break;

            case USERS:
                statsDAO.addTagUsers(message.getTagId(), message.getCount());
                break;

            case ARTICLES:

                statsDAO.addTagArticles(message.getTagId(), message.getCount());
                break;

            case PROBLEMS:

                statsDAO.addTagProblems(message.getTagId(),message.getCount());
                break;

            case QUESTIONS:

                statsDAO.addTagQuestions(message.getTagId(), message.getCount());
                break;

        }

    }

}
