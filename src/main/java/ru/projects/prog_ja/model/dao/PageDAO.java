package ru.projects.prog_ja.model.dao;

public interface PageDAO {

    long countArticles();

    long countQuestions();

    long countProblems();

    long countFacts();

    long countTags();

    long countUsers();
}
