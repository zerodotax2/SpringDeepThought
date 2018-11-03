package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;

import java.util.List;

public interface QuestionWriteService {

    FullQuestionTransfer createQuestion(String title, List<Long> tags, String content, long userId);

    boolean updateQuestion(long id, String title, String html, List<Long> tags, long userId);

    boolean deleteQuestion(long id, long userId);

    boolean updateQuestionRate(long questionId, int rate, long userId);

    CommonAnswerTransfer addAnswer(long questionId, String htmlContent, long userId);

    boolean setRightAnswer(long answerId, long userId) ;

    boolean updateAnswer(long id, String html, long userId) ;

    boolean deleteAnswer(long id, long userId) ;

    boolean updateAnswerRate(long answerId, int rate, long userId) ;
}
