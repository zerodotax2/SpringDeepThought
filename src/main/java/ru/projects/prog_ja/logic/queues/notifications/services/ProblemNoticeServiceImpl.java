package ru.projects.prog_ja.logic.queues.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.ProblemNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.processing.ProblemNoticeQueue;

@Service
@Scope("prototype")
public class ProblemNoticeServiceImpl implements ProblemNoticeService{

    private final ProblemNoticeQueue queue;

    @Autowired
    public ProblemNoticeServiceImpl(ProblemNoticeQueue queue) {
        this.queue = queue;
    }

    @Override
    public void rateNotice(long problemId, long userId) {

        queue.putMessage(new ProblemNoticeMessage(problemId, userId,
                ProblemNoticeMessage.Type.RATE));
    }

    @Override
    public void commentNotice(long problemId, long userId) {

        queue.putMessage(new ProblemNoticeMessage(problemId, userId,
                ProblemNoticeMessage.Type.COMMENT));
    }

    @Override
    public void commentRateNotice(long commentId, long userId) {

        queue.putMessage(new ProblemNoticeMessage(commentId, userId,
                ProblemNoticeMessage.Type.COMMENT_RATE));
    }

    @Override
    public void feedbackNotice(long problemId, long userId) {

        queue.putMessage(new ProblemNoticeMessage(problemId, userId,
                ProblemNoticeMessage.Type.FEEDBACK));
    }

}
