package ru.projects.prog_ja.services.rest.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.view.create.CreateArticleDTO;
import ru.projects.prog_ja.dto.view.update.UpdateArticleDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/articles")
public class RestArticlesService extends AbstractRestService {

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    @Autowired
    public RestArticlesService (ArticleReadService articleReadService,
                                ArticleWriteService articleWriteService){
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }



    @GetMapping
    public ResponseEntity<?> getArticles(@RequestParam(value = "q", required = false) String q,
                                          @RequestParam(value = "sort", defaultValue = "1") String sort,
                                          @RequestParam(value = "start") String start,
                                          @RequestParam(value = "type", defaultValue = "rating") String type){

        if(start == null || start.equals(""))
            return badRequest();
        else if(!start.matches("^\\d+&") || start.length() > 32)
            return incorrectFormat();

        List<CommonArticleTransfer> articles = articleReadService.getArticles(Integer.parseInt(start),
                q, type, sort);
        if(articles == null || articles.size() == 0){
            return notFound();
        }

        return found(articles);

    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreateArticleDTO createArticleDTO, BindingResult bindingResult,
                                        @SessionAttribute("user")UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();
        
        FullArticleTransfer fullArticleTransfer = articleWriteService.createArticle(
                createArticleDTO.getSmallImagePath(), createArticleDTO.getMiddleImagePath(), createArticleDTO.getLargeImagePath(),
                createArticleDTO.getTitle(), createArticleDTO.getSubtitle(), createArticleDTO.getHtmlContent(), createArticleDTO.getTags(),
                userDTO.getId()
        );
        if(fullArticleTransfer == null)
            return serverError();

        return accepted(fullArticleTransfer);
    }
    
    @PutMapping
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdateArticleDTO updateArticleDTO, BindingResult bindingResult,
                                        @SessionAttribute("user")UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();
        
        if(articleWriteService.updateArticle( updateArticleDTO.getArticleId(),
                updateArticleDTO.getSmallImagePath(), updateArticleDTO.getMiddleImagePath(), updateArticleDTO.getLargeImagePath(),
                updateArticleDTO.getTitle(), updateArticleDTO.getSubtitle(), updateArticleDTO.getHtmlContent(), updateArticleDTO.getTags(),
                userDTO.getId())){
            return ok();
        }
        return serverError();
    }

    @PostMapping(value = "/rating")
    public ResponseEntity<?> changeRate(@Valid @RequestBody UpdateRatingDTO updateRatingDTO, BindingResult bindingResult,
                                        @SessionAttribute("user") UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        if(articleWriteService.changeRate(updateRatingDTO.getId(), updateRatingDTO.getRate(), userDTO.getId())){
            return found(updateRatingDTO);
        }
        return serverError();
    }
    
}
