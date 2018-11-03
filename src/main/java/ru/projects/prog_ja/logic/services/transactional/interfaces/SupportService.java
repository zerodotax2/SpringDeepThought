package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.full.FullForumAnswer;
import ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer;

import java.util.List;

public interface SupportService {

    FullForumAnswer ask(String text, long userId);

    List<SmallUserQuestionTransfer> getUserQuestions(int start, int size);

    List<SmallUserQuestionTransfer> getNonAnsweredQuestions(int start, int size);

    boolean answer(long id, String text, long userId);

    FullForumAnswer getFullAnswer(long id);
}
