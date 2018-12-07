package ru.projects.prog_ja.logic.queues.stats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.stats.messages.TagCounterMessage;
import ru.projects.prog_ja.logic.queues.stats.queues.TagStatsQueue;

@Service
@Scope("prototype")
public class TagCounterImpl implements TagCounter{
    
    private final TagStatsQueue queue;

    @Autowired
    public TagCounterImpl(TagStatsQueue queue) {
        this.queue = queue;
    }

    @Override
    public boolean incrementArticles(long tagId, int count) {
        
        return queue.putMessage( new TagCounterMessage(tagId, count,
                TagCounterMessage.Type.ARTICLES));
    }

    @Override
    public boolean incrementQuestions(long tagId, int count) {

        return queue.putMessage( new TagCounterMessage(tagId, count,
                TagCounterMessage.Type.QUESTIONS));
    }

    @Override
    public boolean incrementProblems(long tagId, int count) {

        return queue.putMessage( new TagCounterMessage(tagId, count,
                TagCounterMessage.Type.PROBLEMS));
    }

    @Override
    public boolean incrementFacts(long tagId, int count) {

        return queue.putMessage( new TagCounterMessage(tagId, count,
                TagCounterMessage.Type.FACTS));
    }

    @Override
    public boolean incrementUsers(long tagId, int count) {

        return queue.putMessage( new TagCounterMessage(tagId, count,
                TagCounterMessage.Type.USERS));
    }
}
