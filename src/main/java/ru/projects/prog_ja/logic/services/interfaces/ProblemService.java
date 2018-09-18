package ru.projects.prog_ja.logic.services.interfaces;

import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;

import java.util.List;

public interface ProblemService {

    FullProblemTransfer getFullProblem(long problemId);

    List<SmallProblemTransfer> getSmallProblems(int start, String type, int sort);

    List<SmallProblemTransfer> findSmallProblems(int start, String search, String type, int sort);

    void createProblem(long userId, String title, String content, ProblemDifficult difficult, List<Long> tags);

    void updateProblem(long problemId, String title, String content, ProblemDifficult problemDifficult, List<Long> tags);

    List<SmallProblemTransfer> getByDifficult(int start, String difficult, String type, int sort);

    List<SmallProblemTransfer> getProblemsByUser(int start, long userId, String type, int sort);
}
