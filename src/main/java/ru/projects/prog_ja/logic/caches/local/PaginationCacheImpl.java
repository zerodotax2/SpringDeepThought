package ru.projects.prog_ja.logic.caches.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.caches.interfaces.PaginationCache;
import ru.projects.prog_ja.model.dao.PageDAO;

import javax.annotation.PostConstruct;

@Service
@Scope("singleton")
@EnableScheduling
public class PaginationCacheImpl implements PaginationCache {

    private static long articles = 0;
    private static long questions = 0;
    private static long problems = 0;
    private static long facts = 0;
    private static long tags = 0;
    private static long users = 0;

    private final PageDAO pageDAO;

    @Autowired
    public PaginationCacheImpl(PageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    @PostConstruct
    @Scheduled(fixedRate = 600_000)
    public void init(){

        articles = pageDAO.countArticles();
        questions = pageDAO.countQuestions();
        problems = pageDAO.countProblems();
        facts = pageDAO.countFacts();
        tags = pageDAO.countTags();
        users = pageDAO.countUsers();

    }

    @Override
    public long articlesSize() {
        return articles;
    }

    @Override
    public long questionsSize() {
        return questions;
    }

    @Override
    public long problemsSize() {
        return problems;
    }

    @Override
    public long tagsSize() {
        return tags;
    }

    @Override
    public long factsSize() {
        return facts;
    }

    @Override
    public long usersSize() {
        return users;
    }
}
