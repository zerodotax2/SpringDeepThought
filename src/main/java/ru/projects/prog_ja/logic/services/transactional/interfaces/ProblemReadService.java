package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.view.EditProblemDTO;
import ru.projects.prog_ja.dto.view.PageableContainer;

public interface ProblemReadService {

    FullProblemTransfer getFullProblem(long problemId, UserDTO userDTO);

    PageableContainer getProblems(String page, String query, String difficult, String type, String sort);

    PageableContainer getSmallProblems(String page, String type, String sort);

    PageableContainer findSmallProblems(String page, String search, String type, String sort);

    PageableContainer getByDifficult(String page, String query, String difficult, String type, String sort);

    PageableContainer getProblemsByUser(String page, String size, long userId, String difficult, String query, String type, String sort);

    PageableContainer getProblemsSolvedByUser(String page, String size, long userId, String difficult, String query, String type, String sort);

    PageableContainer getProblemsByTag(String page, String size, long tagId, String difficult, String query, String type, String sort);

    FullSolutionTransfer getProblemSolution(long problemId);

    EditProblemDTO getEditProblem(long problemId);
}
