package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.model.entity.problems.ProblemDifficult;

import java.util.List;

/**
 * data access interface
 * */
public interface ProblemsDAO {

    void createProblem(long userId, String title, String content, List<Long> tags, ProblemDifficult difficult);

    void updateProblem(long problemId, String title, String content, List<Long> tags, ProblemDifficult difficult);

    void updateTitle(long problemId, String title);

    void updateContent(long problemId, String content);

    void updateTags(long problemId, List<Long> tags);

    void updateDifficult(long problemId, ProblemDifficult problemDifficult);

    List<SmallProblemTransfer> getSmallProblems(int start, int size, String type, int sort);

    List<SmallProblemTransfer> getProblemsByDifficult(int start, int size, ProblemDifficult problemDifficult,String type, int sort);

    List<SmallProblemTransfer> getSmallProblemsByTag(int start, int size, long tagID, String orderField, int sort);

    List<SmallProblemTransfer> getSmallProblemsByUser(int start, int size, long userId, String type, int sort);

    List<SmallProblemTransfer> findSmallProblems(int start, int size, String search, String type, int sort);

    FullProblemTransfer getOneProblem(long problemId);

    void deleteProblem(long problemId);

    long getProblemsNum();
}
