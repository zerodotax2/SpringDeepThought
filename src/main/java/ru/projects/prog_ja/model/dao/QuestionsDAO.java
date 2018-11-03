package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
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
    List<SmallQuestionTransfer> findSmallQuestions(int start, int size, String search, String orderField, int sort);

    /**
     * @return list with small questions started from
     * @param start
     * */
    List<SmallQuestionTransfer> getSmallQuestions(int start, int size, String orderField, int sort);
    /**
     * find full questions with
     * @param search
     * */
    List<CommonQuestionTransfer> findQuestions(int start, int size, String search, String orderField, int sort);

    /**
     * @return last questions
     * */
    List<CommonQuestionTransfer> getQuestions(int start, int size, String orderField, int sort);

    List<SmallQuestionTransfer> getSmallQuestionsByTag(int start, int size, long tagID, String orderField, int sort);

    List<CommonQuestionTransfer> getCommonQuestionsByTag(int start, int size, long tagID, String orderField, int sort);

    List<SmallQuestionTransfer> getSmallQuestionsByUser(int start, int size, long userId, String orderField, int sort);

    List<ViewAnswerTransfer> getSmallAnswersByUser(int start, int size, long userId, String orderField, int sort);
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

    boolean updateQuestionRate(long questionId, int rate, long userId);
    
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

    long getQuestionsNum();
}
