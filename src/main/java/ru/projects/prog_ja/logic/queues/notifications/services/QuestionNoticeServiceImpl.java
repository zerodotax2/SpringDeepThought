package ru.projects.prog_ja.logic.queues.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.QuestionNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.processing.QuestionNoticeQueue;

@Service
@Scope("prototype")
public class QuestionNoticeServiceImpl implements QuestionNoticeService{

    private final QuestionNoticeQueue queue;

    @Autowired
    public QuestionNoticeServiceImpl(QuestionNoticeQueue queue) {
        this.queue = queue;
    }

    @Override
    public void rateNotice(long questionId, long userId) {

        queue.putMessage(new QuestionNoticeMessage(questionId, userId,
                QuestionNoticeMessage.Type.RATE));

    }

    @Override
    public void answerNotice(long questionId, long userId) {

        queue.putMessage(new QuestionNoticeMessage(questionId, userId,
                QuestionNoticeMessage.Type.ANSWER));

    }

    @Override
    public void rightAnswerNotice(long answerId) {

        queue.putMessage(new QuestionNoticeMessage(answerId, 0,
                QuestionNoticeMessage.Type.RIGHT));

    }

    @Override
    public void answerRateNotice(long answerId, long userId) {

        queue.putMessage(new QuestionNoticeMessage(answerId, userId,
                QuestionNoticeMessage.Type.ANSWER_RATE));

    }

}
