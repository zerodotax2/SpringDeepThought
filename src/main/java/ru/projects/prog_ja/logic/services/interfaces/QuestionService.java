package ru.projects.prog_ja.logic.services.interfaces;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;

import java.util.List;

public interface QuestionService {

    void createQuestion(String title, List<Long> tags, String content, long userId);

    void updateQuestion(long id, String title, String html, List<Long> tags);

    List<CommonQuestionTransfer> getQuestions(int start, String type, int sort);

    List<CommonQuestionTransfer> findCommonQuestions(int start, String search, String type, int sort);

    List<SmallQuestionTransfer> findSmallQuestions(int start, String search, String type, int sort);

    FullQuestionTransfer getOneQuestion(long id);

    List<SmallQuestionTransfer> getSmallQuestions(int start, String type, int sort);

    CommonAnswerTransfer addAnswer(long articleId, String htmlContent, long userId);

    void updateAnswer(long id, String html);

    List<SmallQuestionTransfer> getQuestionsByUser(int start, long userId, String type, int sort);
}
