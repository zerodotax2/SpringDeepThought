package ru.projects.prog_ja.logic.queues.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.FactNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.processing.FactNoticeQueue;

@Service
@Scope("prototype")
public class FactNoticeServiceImpl implements FactNoticeService{

    private final FactNoticeQueue queue;

    @Autowired
    public FactNoticeServiceImpl(FactNoticeQueue queue) {
        this.queue = queue;
    }

    @Override
    public void factRate(long factId, long userId) {

        queue.putMessage(new FactNoticeMessage(factId, userId,
                FactNoticeMessage.Type.RATE));
    }

}
