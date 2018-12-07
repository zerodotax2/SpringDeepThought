package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;

public interface QuestionReadService {


    PageableContainer getQuestions(String page, String query, String type, String sort);

    PageableContainer getDefaultQuestions(String page, String type, String sort);

    PageableContainer findCommonQuestions(String page, String search, String type, String sort);

    PageableContainer findSmallQuestions(String page, String search, String type, String sort);

    FullQuestionTransfer getOneQuestion(long id, UserDTO userDTO);

    PageableContainer getSmallQuestions(String page, String query, String type, String sort);

    PageableContainer getDefaultSmallQuestions(String page, String type, String sort);

    PageableContainer getQuestionsByUser(String page, String size, long userId, String query, String type, String sort);

    PageableContainer getAnswersByUser(int start, String size, long userId, String query, String type, String sort);

    PageableContainer getQuestionsByTag(String page, String size, long tagId, String query, String type, String sort);
}
