package ru.projects.prog_ja.services.rest.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping("/services/articles")
public class RestArticlesBySomethingService  extends AbstractRestService {

    private final ArticleReadService articleReadService;

    @Autowired
    public RestArticlesBySomethingService(ArticleReadService articleReadService){
        this.articleReadService = articleReadService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getArticlesByUser(@PathVariable("id") long id,
                                               @RequestParam(name = "size", defaultValue = "6") String size,
                                               @RequestParam(name = "q", required = false) String q,
                                               @RequestParam(name = "page", defaultValue = "1") String page,
                                               @RequestParam(name = "type", defaultValue = "rating") String rating,
                                               @RequestParam(name = "sort", defaultValue = "1")String sort){

        return found(articleReadService.getSmallArticlesByUser(page, size, id, q,rating, sort));
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getArticlesByTag(@PathVariable("id") long id,
                                              @RequestParam(name = "size", defaultValue = "6") String size,
                                              @RequestParam(name = "q", required = false) String q,
                                              @RequestParam(name = "page", defaultValue = "1") String page,
                                              @RequestParam(name = "type", defaultValue = "rating") String rating,
                                              @RequestParam(name = "sort", defaultValue = "1")String sort){


        return found(articleReadService.getSmallArticlesByTag(page,size,id,q, rating, sort));
    }
}
