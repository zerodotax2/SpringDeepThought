package ru.projects.prog_ja.model.dao;

public interface ViewsDAO {

    boolean addArticleView(long articleId, int views) throws Exception;

    boolean addQuestionView(long questionId, int views) throws Exception;

    boolean addProblemView(long problemId, int views) throws Exception;

}
