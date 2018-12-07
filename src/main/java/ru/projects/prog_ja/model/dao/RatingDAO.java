package ru.projects.prog_ja.model.dao;

public interface RatingDAO {

    boolean updateUserRate(long userId, int rate) throws Exception;

    boolean updateArticleOwnerRate(long articleId, int rate, long userId) throws Exception;

    boolean updateArticleCommentOwnerRate(long commentId, int rate, long userId) throws Exception;

    boolean updateQuestionOwnerRate(long questionId, int rate, long userId) throws Exception;

    boolean updateAnswerOwnerRate(long answerId, int rate, long userId) throws Exception;

    boolean updateProblemOwnerRate(long problemId, int rate, long userId) throws Exception;

    boolean updateProblemCommentOwnerRate(long commentId, int rate, long userId) throws Exception;

    boolean updateFactOwnerRate(long factId, int rate, long userId) throws Exception;

    boolean updateTagOwnerRate(long tagId, int rate, long userId) throws Exception;

}
