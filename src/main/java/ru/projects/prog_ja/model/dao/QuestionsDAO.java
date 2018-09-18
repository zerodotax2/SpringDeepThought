package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.commons.CommonQuestionTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;

import java.util.List;

public interface QuestionsDAO {

    /**
     * create question with this parameters
     * */
    void createQuestion(String title, List<Long> tags, String htmlText, long userId);

    /**
     * delete question with
     * @param id
     * */
    void deleteQuestion(long id);

    /**
     * find small questions with
     * @param search
     * */
    List<SmallQuestionTransfer> findSmallQuestions(int start, int size, String search, String type, int sort);

    /**
     * find full questions with
     * @param search
     * */
    List<CommonQuestionTransfer> findQuestions(int start, int size, String search, String type, int sort);

    /**
     * @return last questions
     * */
    List<CommonQuestionTransfer> getQuestions(int start, int size, String type, int sort);

    List<SmallQuestionTransfer> getSmallQuestionsByTag(int start, int size, long tagID, String orderField, int sort);

    List<CommonQuestionTransfer> getCommonQuestionsByTag(int start, int size, long tagID, String orderField, int sort);

    List<SmallQuestionTransfer> getSmallQuestionsByUser(int start, int size, long userId, String type, int sort);
    /**
     * change question with this params
     * */
    void updateQuestion(long id, String title, List<Long> tags, String htmlText);

    /**
     * add answer to this question
     * */
    long addAnswer(String htmlText, long questionContentId, long userId);

    /**
     * change answer with this params
     * */
    void updateAnswer(long id, String htmlText);

    /**
     * delete answer from this question
     * */
    void deleteAnswer(long id);

    /**
     * @return full questions
     * */
    FullQuestionTransfer getFullQuestion(long id);

    /**
     * @return list with small questions started from
     * @param start
     * */
    List<SmallQuestionTransfer> getSmallQuestions(int start, int size, String type, int sort);

    CommonAnswerTransfer getAnswer(long id);

    long getQuestionsNum();
}
