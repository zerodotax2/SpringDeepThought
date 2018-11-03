package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Контроллер для навигационной панели,
 * будет отдавать список популярных тегов, статей,
 * рейтинг пользователей
 * */
@RestController
@RequestMapping(value = "/services/nav")
@Scope("singleton")
@EnableScheduling
public class NavigationPanelController {

    /**
     * Сервисы необходимые для получения данных,
     * которые будут отображаться на странице
     * */
    private final UserReadService userReadService;
    private final TagsReadService tagsReadService;
    private final ArticleReadService articleReadService;

    /**
     * Списки, который будут отображаться во view,
     * навигационной панели
     * */
    private List<SmallArticleTransfer> popularArticles = new ArrayList<>();
    private List<SmallTagTransfer> popularTags = new ArrayList<>();
    private List<SmallUserTransfer> popularUsers = new ArrayList<>();

    @Autowired
    public NavigationPanelController(UserReadService userReadService,
                                     TagsReadService tagsReadService,
                                      ArticleReadService articleReadService) {
        this.userReadService = userReadService;
        this.tagsReadService = tagsReadService;
        this.articleReadService = articleReadService;
    }

    /**
     * Инициализируем в кеш все списки
     * */
    @PostConstruct
    public void init(){

        List<SmallArticleTransfer> popularArticlesTemp = articleReadService.getPopularArticles(0);
        if(popularArticlesTemp != null){
            this.popularArticles = popularArticlesTemp;
        }

        List<SmallTagTransfer> popularTagsTemp = tagsReadService.getPopularTags();
        if(popularTagsTemp != null){
            this.popularTags = popularTagsTemp;
        }

        List<SmallUserTransfer> popularUsersTemp = userReadService.getSmallUsers(0, "rating", "1");
        if(popularUsersTemp != null){
            this.popularUsers = popularUsersTemp;
        }

    }

    /**
     *
     * Каждый час обновляем все списки в кеше
     */
    @Scheduled(fixedRate = 3_600_000)
    public void update(){
        init();
    }

    /**
     * Даём возможность получить те же списки из ajax запросов
     * */

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallUserTransfer>> getPopularUsersJson(){

        if(this.popularUsers == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularUsers, HttpStatus.OK);
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallArticleTransfer>> getPopularArticlesJson(){

        if(this.popularArticles == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularArticles, HttpStatus.OK);
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallTagTransfer>> getPopularTagsJson(){

        if(this.popularTags == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularTags, HttpStatus.OK);
    }

    public List<SmallArticleTransfer> getPopularArticles() {
        return popularArticles;
    }

    public List<SmallTagTransfer> getPopularTags() {
        return popularTags;
    }

    public List<SmallUserTransfer> getPopularUsers() {
        return popularUsers;
    }
}
