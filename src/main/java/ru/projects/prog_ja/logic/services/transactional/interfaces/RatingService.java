package ru.projects.prog_ja.logic.services.transactional.interfaces;

public interface RatingService {

    void updateUserRate(long userId, int rate);

    void updateArticleOwnerRate(long articleId, int rate, long userId);

    void updateArticleCommentOwnerRate(long commentId, int rate, long userId);

    void updateQuestionOwnerRate(long questionId, int rate, long userId);

    void updateAnswerOwnerRate(long answerId, int rate, long userId);

    void updateProblemOwnerRate(long problemId, int rate, long userId);

    void updateProblemCommentOwnerRate(long commentId, int rate, long userId);

    void updateFactOwnerRate(long factId, int rate);

    void updateTagOwnerRate(long tagId, int rate);

}
