package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;

import java.util.List;

public interface ProblemReadService {

    FullProblemTransfer getFullProblem(long problemId);

    List<SmallProblemTransfer> getProblems(int start, String query, String difficult, String type, String sort);

    List<SmallProblemTransfer> getSmallProblems(int start, String type, String sort);

    List<SmallProblemTransfer> findSmallProblems(int start, String search, String type, String sort);

    List<SmallProblemTransfer> getByDifficult(int start, String query, String difficult, String type, String sort);

    BySomethingContainer getProblemsByUser(int start, String size, long userId, String type, String sort);

    BySomethingContainer getProblemsSolvedByUser(int start, String size, long userId, String type, String sort);

    BySomethingContainer getProblemsByTag(int start, String size, long tagId, String type, String sort);

    FullSolutionTransfer getProblemSolution(long problemId);
}
