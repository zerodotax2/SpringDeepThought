package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;

import java.util.List;

public interface QuestionReadService {


    List<CommonQuestionTransfer> getQuestions(int start, String query, String type, String sort);

    List<CommonQuestionTransfer> getDefaultQuestions(int start, String type, String sort);

    List<CommonQuestionTransfer> findCommonQuestions(int start, String search, String type, String sort);

    List<SmallQuestionTransfer> findSmallQuestions(int start, String search, String type, String sort);

    FullQuestionTransfer getOneQuestion(long id);

    List<SmallQuestionTransfer> getSmallQuestions(int start, String query, String type, String sort);

    List<SmallQuestionTransfer> getDefaultSmallQuestions(int start, String type, String sort);

    BySomethingContainer getQuestionsByUser(int start, String size, long userId, String type, String sort);

    BySomethingContainer getAnswersByUser(int start, String size, long userId, String type, String sort);

    BySomethingContainer getQuestionsByTag(int start, String size, long tagId, String type, String sort);
}
