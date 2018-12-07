package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.full.FullForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;

import java.util.List;

public interface SupportDAO {

    FullForumAnswer addForumQuestion(String text, long userID);

    List<SmallUserQuestionTransfer> getForumQuestions(int start, int size);

    List<SmallUserQuestionTransfer> getNonAnsweredForumQuestions(int start, int size);

    boolean answerForumQuestion(long userQuestionId, String text, long userId);

    boolean removeForumQuestion(long id, long userId);

    boolean removeForumAnswer(long id, long userId);

    SmallUserQuestionTransfer getSmallQuestion(long id);

    SmallForumAnswer getSmallAnswer(long id);

    FullForumAnswer getOneQuestion(long id);

    NoticeEntityTemplateDTO getNoticeTemplate(long answerId);
}
