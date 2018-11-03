package ru.projects.prog_ja.services.rest.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.List;

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
                                               @RequestParam(value = "size", defaultValue = "6") String size){

        BySomethingContainer container = articleReadService.getArticlesByUser(0, size, id, "rating", "1");
        if(container == null){
            return  notFound();
        }

        return found(container);
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getArticlesByTag(@PathVariable("id") long id,
                                              @RequestParam(value = "size", defaultValue = "6") String size){


        BySomethingContainer container = articleReadService.getArticlesByTag(0,size, id, "rating", "1");
        if(container == null){
            return  notFound();
        }


        return found(container);
    }
}
