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
import ru.projects.prog_ja.logic.services.interfaces.ArticleService;
import ru.projects.prog_ja.logic.services.interfaces.TagsService;
import ru.projects.prog_ja.logic.services.interfaces.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 * Контроллер для навигационной панели,
 * будет отдавать список популярных тегов, статей,
 * рейтинг пользователей
 * */
@RestController
@Scope("singleton")
@EnableScheduling
public class NavigationPanelController {

    /**
     * Сервисы необходимые для получения данных,
     * которые будут отображаться на странице
     * */
    private final UserService userService;
    private final TagsService tagsService;
    private final ArticleService articleService;

    /**
     * Списки, который будут отображаться во view,
     * навигационной панели
     * */
    private List<SmallArticleTransfer> popularArticles;
    private List<SmallTagTransfer> popularTags;
    private List<SmallUserTransfer> popularUsers;

    public NavigationPanelController(@Autowired UserService userService,
                                     @Autowired TagsService tagsService,
                                     @Autowired ArticleService articleService) {
        this.userService = userService;
        this.tagsService = tagsService;
        this.articleService = articleService;
    }

    /**
     * Инициализируем в кеш все списки
     * */
    @PostConstruct
    public void init(){

        this.popularArticles = articleService.getPopularArticles(0);
        this.popularTags = tagsService.getPopularTags();
        this.popularUsers = userService.getSmallUsers(0, "rating", 1);

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

    @RequestMapping(value = "/nav/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallUserTransfer>> getPopularUsers(){

        if(this.popularUsers == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularUsers, HttpStatus.OK);
    }

    @RequestMapping(value = "/nav/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallArticleTransfer>> getPopularArticles(){

        if(this.popularArticles == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularArticles, HttpStatus.OK);
    }

    @RequestMapping(value = "/nav/tags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SmallTagTransfer>> getPopularTags(){

        if(this.popularTags == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.popularTags, HttpStatus.OK);
    }


}
