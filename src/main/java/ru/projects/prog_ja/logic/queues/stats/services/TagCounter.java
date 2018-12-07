package ru.projects.prog_ja.logic.queues.stats.services;

public interface TagCounter {
    
    boolean incrementArticles(long tagId, int count);
    
    boolean incrementQuestions(long tagId, int count);
    
    boolean incrementProblems(long tagId, int count);
    
    boolean incrementFacts(long tagId, int count);
    
    boolean incrementUsers(long tagId, int count);
    
}
