package ru.projects.prog_ja.logic.queues.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.ForumNotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.processing.ForumNoticeQueue;

@Service
@Scope("prototype")
public class ForumNoticeServiceImpl implements ForumNoticeService{

    private final ForumNoticeQueue queue;

    @Autowired
    public ForumNoticeServiceImpl(ForumNoticeQueue queue) {
        this.queue = queue;
    }

    @Override
    public void accountCreateNotice(long userId) {

        queue.putMessage(new ForumNotificationMessage(0, userId,
                ForumNotificationMessage.Type.CREATE));
    }

    @Override
    public void answerForumQuestion(long questionId, long userId) {

        queue.putMessage(new ForumNotificationMessage(questionId, userId,
                ForumNotificationMessage.Type.FORUM_ANSWER));
    }

}
