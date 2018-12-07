package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.exceptions.AlreadyExistException;
import ru.projects.prog_ja.exceptions.RepeatVotedException;

import java.util.List;

public interface ProblemWriteService {

    FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, String difficult, List<Long> tags);

    boolean updateProblem(long problemId, String title, String content, String solution, String answer, String dififcult, List<Long> tags, long userId);

    boolean changeRate(long problemId, int rate, long userId) throws RepeatVotedException;

    CommonCommentTransfer addComment(long articleId, String text, long userId);

    boolean updateComment(long commentId, String text, long userId);

    boolean updateCommentRate(long commentId, int rate, long userId) throws RepeatVotedException ;

    boolean removeComment(long commentId, long userId);

    boolean sendFeedback(long problemId, String feedback, long userId);

    boolean checkAnswer(long problemId, String answer, long userId) throws AlreadyExistException;

}
