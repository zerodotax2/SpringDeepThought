package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallFeedbackDTO;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.EditProblemDTO;
import ru.projects.prog_ja.dto.view.ProblemDifficult;

import java.util.List;

/**
 * data access interface
 * */
public interface ProblemsDAO {

    FullProblemTransfer createProblem(long userId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult);

    boolean updateProblem(long problemId, String title, String content, String solution, String answer, List<Long> tags, ProblemDifficult difficult, long userId);

    boolean updateTitle(long problemId, String title);

    boolean updateContent(long problemId, String content);

    boolean updateSolution(long problemId, String solution);

    boolean updateAnswer(long problemId, String answer);

    boolean updateTags(long problemId, List<Long> tags);

    boolean updateDifficult(long problemId, ProblemDifficult problemDifficult);

    String getTitle(long articleId);

    NoticeCommentTemplateDTO getNoticeCommentTemplate(long articleId);

    NoticeEntityTemplateDTO getNoticeTemplate(long articleId);

    boolean addProblemSolveUser(long problemId, long userId);

    List<SmallTagTransfer> getTagsByProblem(long problemId);

    PageableEntity getSmallProblems(int start, int size, String type, int sort);

    PageableEntity getProblemsByDifficult(int start, int size, ProblemDifficult problemDifficult,String type, int sort);

    PageableEntity findProblemsByDifficult(int start, int size, String query, ProblemDifficult problemDifficult,String type, int sort);

    PageableEntity getSmallProblemsByTag(int start, int size, long tagID, ProblemDifficult difficult, String query, String orderField, int sort);

    PageableEntity getSmallProblemsByUser(int start, int size, long userId, ProblemDifficult difficult, String query, String type, int sort);

    PageableEntity findSmallProblems(int start, int size, String search, String type, int sort);

    PageableEntity getUserSolvedProblems(int start, int size, long userId, ProblemDifficult difficult,String query, String type, int sort);

    FullProblemTransfer getOneProblem(long problemId);

    EditProblemDTO getEditProblem(long problemId);

    FullSolutionTransfer getProblemSolution(long problemId);

    List<CommonCommentTransfer> getProblemComments(long problemId);

    boolean addProblemFeedback(long problemId, String text, long userId);

    List<SmallFeedbackDTO> getProblemFeedback(long problemId, int start, int size);

    String getProblemAnswer(long problemId);

    boolean isAlreadyDecide(long problemId, long userId);

    boolean deleteProblem(long problemId, long userId);

    List<Long> getTagsIDByProblem(long problemId);

    long getProblemsNum();

    boolean isProblemVoted(long problemId, long userId);

    boolean isProblemCommentVoted(long problemCommentId, long userId);

    boolean changeRate(long problemId, int rate, long userId);

    boolean changeCommentRate(long commentId, int rate, long userId);

    CommonCommentTransfer addComment(long problemId, String comment, long userId);

    boolean updateComment(long commentId, String comment, long userId);

    boolean removeComment(long commentId, long userId);

}
