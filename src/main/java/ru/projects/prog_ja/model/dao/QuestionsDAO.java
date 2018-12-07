package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;

import java.util.List;

public interface QuestionsDAO {


    /**
     * @return full questions
     * */
    FullQuestionTransfer getFullQuestion(long id);
    /**
     * find small questions with
     * @param search
     * */
    PageableEntity findSmallQuestions(int start, int size, String search, String orderField, int sort);

    /**
     * @return list with small questions started from
     * @param start
     * */
    PageableEntity getSmallQuestions(int start, int size, String orderField, int sort);
    /**
     * find full questions with
     * @param search
     * */
    PageableEntity findQuestions(int start, int size, String search, String orderField, int sort);

    /**
     * @return last questions
     * */
    PageableEntity getQuestions(int start, int size, String orderField, int sort);

    PageableEntity getSmallQuestionsByTag(int start, int size, long tagID, String query, String orderField, int sort);

    PageableEntity getCommonQuestionsByTag(int start, int size, long tagID, String query, String orderField, int sort);

    PageableEntity getSmallQuestionsByUser(int start, int size, long userId, String query, String orderField, int sort);

    List<ViewAnswerTransfer> getSmallAnswersByUser(int start, int size, long userId, String query, String orderField, int sort);
    /**
     * change question with this params
     * */
    boolean updateQuestion(long id, String title, List<Long> tags, String htmlText, long userId);

    /**
     * create question with this parameters
     * */
    FullQuestionTransfer createQuestion(String title, List<Long> tags, String htmlText, long userId);

    /**
     * delete question with
     * @param id
     * */
    boolean deleteQuestion(long id, long userId);

    boolean isQuestionVoted(long questionId, long userId);

    boolean isAnswerVoted(long answerId, long userId);

    boolean updateQuestionRate(long questionId, int rate, long userId);

    String getTitle(long articleId);

    NoticeCommentTemplateDTO getNoticeCommentTemplate(long commentId);

    NoticeEntityTemplateDTO getNoticeTemplate(long questionId);

    /**
     * add answer to this question
     * */
    CommonAnswerTransfer addAnswer(String htmlText, long questionContentId, long userId);


    CommonAnswerTransfer getAnswer(long id);

    boolean setRightAnswer(long answerId, long userId);
    /**
     * change answer with this params
     * */
    boolean updateAnswer(long id, String htmlText, long userId);

    /**
     * delete answer from this question
     * */
    boolean deleteAnswer(long id, long userId);

    boolean updateAnswerRate(long answerId, int rate, long userId);

    List<Long> getTagsByQuestion(long questionId);

    List<CommonAnswerTransfer> getQuestionAnswers(long questionId);

    List<SmallTagTransfer> getSmallQuestionTags(long questionId);

    long getQuestionsNum();
}
