package ru.projects.prog_ja.logic.queues.stats.services;

public interface UserCounter {
    
    boolean incrementArticles(long userId, int count);
    
    boolean incrementQuestions(long userId, int count);
    
    boolean incrementAnswers(long userId, int count);
    
    boolean incrementProblemsSolved(long userId, int count);
    
    boolean incrementProblemsCreated(long userId, int count);
    
    boolean incrementFacts(long userId, int count);
    
    boolean incrementTags(long userId, int count);
    
    boolean incrementComments(long userId, int count);

    boolean incrementNotices(long userId, int count);
}
