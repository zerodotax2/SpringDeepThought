package ru.projects.prog_ja.logic.caches.interfaces;

public interface PaginationCache {

    long articlesSize();

    long questionsSize();

    long problemsSize();

    long tagsSize();

    long factsSize();

    long usersSize();

}
