package ru.projects.prog_ja.model.dao;

public interface StatsDAO {

    /**
     * Problem
     * */
    boolean addProblemAttempts(long problemId, int attempts);

    boolean addProblemSolved(long problemId, int solved);

    /**
     * Tags
     * */
    boolean addTagFacts(long tagId, int facts);

    boolean addTagArticles(long tagId, int articles);

    boolean addTagQuestions(long tagId, int questions);

    boolean addTagProblems(long tagId, int problems);

    boolean addTagUsers(long tagId, int users);

    /**
     * Users
     * */

    boolean addUserArticles(long userId, int articles);

    boolean addUserQuestions(long userId, int questions);

    boolean addUserAnswers(long userId, int answers);

    boolean addUserProblemsSolved(long userId, int problemsSolved);

    boolean addUserProblemsCreated(long userId, int problemsCreated);

    boolean addUserComments(long userId, int comments);

    boolean addUserFacts(long userId, int facts);

    boolean addUserTags(long userId, int tags);

    boolean addUserNotice(long userId, int notices);
}
