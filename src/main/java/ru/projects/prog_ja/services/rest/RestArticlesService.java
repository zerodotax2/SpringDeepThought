package ru.projects.prog_ja.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.RenderDTO;
import ru.projects.prog_ja.dto.view.SearchDTO;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.dto.view.create.CreateArticleDTO;
import ru.projects.prog_ja.logic.services.interfaces.ArticleService;
import ru.projects.prog_ja.logic.services.interfaces.TokenService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/services/articles")
public class RestArticlesService extends AbstractRestService {

    private final ArticleService articleService;
    private final TokenService tokenService;

    public RestArticlesService (@Autowired ArticleService articleService,
                                @Autowired TokenService tokenService){
        this.articleService = articleService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProblemsByUser(@PathVariable("id") long id){

        List<SmallArticleTransfer> problems = articleService.getArticlesByUser(id);
        if(problems == null){
            return  serverError();
        }

        if(problems.size() == 0){
            return  notFound();
        }

        return found(problems);
    }

    @GetMapping
    public ResponseEntity<?> getArticles(@RequestBody RenderDTO articlesRenderDTO){

        /*
         * Получаем обычные посты, начиная со start
         * */
        List<CommonArticleTransfer> posts = articleService.getMiddleArticles(articlesRenderDTO.getStart(),
                articlesRenderDTO.getType(),
                articlesRenderDTO.getSort());

        if(posts == null){
            return serverError();
        }

        return found(posts);

    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreateArticleDTO createArticleDTO, BindingResult bindingResult,
                                        @CookieValue("secured_token") String token){


        /*
         * Проверяем валидость данных
         * */
        if(bindingResult.hasErrors()){
            return badRequest();
        }

        /*
         * Парсим теги
         * */
        JsonArray tags = parseJsonToArray(createArticleDTO.getTagsSTR());

        if(tags.size()>5){
            return badRequest();
        }
        List<Long> tagsIDs = new ArrayList<>();
        for(JsonValue value : tags){
            String tagID = value.toString();
            if(!tagID.matches("^\\d+$") || tagID.length() > 64){
                return incorrectFormat();
            }
            tagsIDs.add(Long.parseLong(value.toString()));
        }

        /*
         * Проверяем, что пользователь действительно тот,
         * за кого себя выдаёт
         * */
        if(!tokenService.authenticateUserToken(createArticleDTO.getUserId(), token)){
            return accessDenied();
        }


        long id = 0;
        try {
            id =  articleService.createArticle(new ThreeImagesPathDTO(createArticleDTO.getSmallImagePath(),
                            createArticleDTO.getMiddleImagePath(), createArticleDTO.getLargeImagePath()),
                    createArticleDTO.getTitle(), createArticleDTO.getSubtitle(), createArticleDTO.getHtmlContent(), tagsIDs, createArticleDTO.getUserId()).get(5, TimeUnit.SECONDS);
        }catch (InterruptedException | ExecutionException e) {
            serverError();
        }  catch (TimeoutException e) {
            return timeout();
        }

        if(id == 0){
            return serverError();
        }

        FullArticleTransfer postTransfer = articleService.getArticleByID(id);
        if(postTransfer == null){
            return serverError();
        }

        return accepted(postTransfer);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findArticles(@Valid @RequestBody SearchDTO searchDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List posts;

        switch (searchDTO.getObjectSize()) {
            case "small":

                posts = articleService.findSmallArticles(searchDTO.getStart(), searchDTO.getSearch(), searchDTO.getType(), searchDTO.getSort());
                break;
            case "common":

                posts = articleService.findArticles(searchDTO.getStart(), searchDTO.getSearch(), searchDTO.getType(), searchDTO.getSort());
                break;
            default:
                return badRequest();
        }

        if(posts == null){
            return serverError();
        }


        return found(posts);
    }


}
