package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.rate.RateMessageType;
import ru.projects.prog_ja.logic.queues.rate.UserRateMessage;
import ru.projects.prog_ja.logic.queues.rate.UserRatingQueue;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;

@Service
@Scope("prototype")
public class RatingServiceImpl implements RatingService {

    private final UserRatingQueue ratingQueue;


    @Autowired
    public RatingServiceImpl(UserRatingQueue ratingQueue) {
        this.ratingQueue = ratingQueue;
    }

    @Override
    public void updateUserRate(long userId, int rate) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.USER,
                userId, rate, userId));
    }

    @Override
    public void updateArticleOwnerRate(long articleId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.ARTICLE_OWNER,
                articleId, rate, userId));
    }

    @Override
    public void updateArticleCommentOwnerRate(long commentId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.ARTICLE_COMMENT_OWNER,
                commentId, rate, userId));
    }

    @Override
    public void updateQuestionOwnerRate(long questionId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.QUESTION_OWNER,
                questionId, rate, userId));
    }

    @Override
    public void updateAnswerOwnerRate(long answerId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.ANSWER_OWNER,
                answerId, rate, userId));
    }

    @Override
    public void updateProblemOwnerRate(long problemId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.PROBLEM_OWNER,
                problemId, rate, userId));
    }

    @Override
    public void updateProblemCommentOwnerRate(long commentId, int rate, long userId) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.PROBLEM_COMMENT_OWNER,
                commentId, rate, userId));
    }

    @Override
    public void updateFactOwnerRate(long factId, int rate) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.FACT_OWNER,
                factId, rate, factId));
    }

    @Override
    public void updateTagOwnerRate(long tagId, int rate) {

        ratingQueue.putMessage(new UserRateMessage(RateMessageType.TAG_OWNER,
                tagId, rate, tagId));
    }
}
