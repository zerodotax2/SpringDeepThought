package ru.projects.prog_ja.logic.queues.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.model.dao.ViewsDAO;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("singleton")
@EnableScheduling
public class ViewsQueueImpl implements ViewsQueue {

    private final ViewsDAO viewsDAO;

    private Map<Long, Integer> articleViews = new ConcurrentHashMap<>();
    private Map<Long, Integer> questionViews = new ConcurrentHashMap<>();
    private Map<Long, Integer> problemViews = new ConcurrentHashMap<>();

    @Autowired
    public ViewsQueueImpl(ViewsDAO viewsDAO) {
        this.viewsDAO = viewsDAO;
    }


    @Override
    public void addArticleView(long articleId) {

        articleViews.merge(articleId,1,
                (a, b) -> a + b);
    }

    @Override
    public void addQuestionView(long questionId) {

        questionViews.merge(questionId,1,
                (a, b) -> a + b);
    }

    @Override
    public void addProblemView(long problemId) {

        problemViews.merge(problemId,1,
                (a, b) -> a + b);
    }

    @Scheduled(fixedRate = 60_000)
    public void updateViews(){

        Map<Long, Integer> tempArticleMap = articleViews;
        articleViews = new ConcurrentHashMap<>();

        Set<Map.Entry<Long, Integer>> articleEntries = tempArticleMap.entrySet();
        for(Map.Entry<Long, Integer> message : articleEntries){
            try {

                viewsDAO.addArticleView(message.getKey(), message.getValue());
            } catch (Exception e) {

                articleViews.put(message.getKey(), message.getValue());
            }
        }

        Map<Long, Integer> tempQuestionsMap = questionViews;
        questionViews = new ConcurrentHashMap<>();

        Set<Map.Entry<Long, Integer>> questionEntries = tempQuestionsMap.entrySet();
        for(Map.Entry<Long, Integer> message : questionEntries){
            try {

                viewsDAO.addQuestionView(message.getKey(), message.getValue());

            } catch (Exception e) {

                questionViews.put(message.getKey(), message.getValue());
            }
        }

        Map<Long, Integer> tempProblemMap = problemViews;
        problemViews = new ConcurrentHashMap<>();

        Set<Map.Entry<Long, Integer>> problemEntries = tempProblemMap.entrySet();
        for(Map.Entry<Long, Integer> message : problemEntries){
            try {

                viewsDAO.addProblemView(message.getKey(), message.getValue());

            } catch (Exception e) {

                problemViews.put(message.getKey(), message.getValue());
            }
        }

    }
}
