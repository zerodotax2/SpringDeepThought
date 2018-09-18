package ru.projects.prog_ja.services.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.create.CreateTagDTO;
import ru.projects.prog_ja.logic.services.interfaces.TokenService;
import ru.projects.prog_ja.logic.singletons.interfaces.FlatColorsLocal;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "/services/tags")
public class RestTagsService extends AbstractRestService {

    private final TagsCache tagsCache;
    private final TokenService tokenService;
    private final FlatColorsLocal flatColorsLocal;

    public RestTagsService(@Autowired TagsCache tagsCache,
                           @Autowired TokenService tokenService,
                           @Autowired FlatColorsLocal flatColorsLocal){
        this.tagsCache = tagsCache;
        this.tokenService = tokenService;
        this.flatColorsLocal = flatColorsLocal;
    }


    /*
    * Возвращаем по get запросу JSON лист популярных тегов
    * */
    @GetMapping
    public ResponseEntity<?> getPopularTags(){

        List<SmallTagTransfer> popularTags = tagsCache.getPopularTags();
        if(popularTags == null){
            return serverError();
        }

        return found(popularTags);
    }

    /*
    * Создаём тег с переданными параметрами
    * */
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagDTO createTagDTO, BindingResult bindingResult,
                                       @CookieValue("secured_token") String token){


        if(bindingResult.hasErrors()){
            return badRequest();
        }
        /*
        * Проверяем, что юзер тот за кого себя выдаёт
        * */
        if(!tokenService.authenticateUserToken(createTagDTO.getUser_id(), token)) {
            return accessDenied();
        }
        /*
        * Достаём имя и описание и пытаемся создать,
        * в будущем при не удачном создании будет вылетать ошибка,
        * а пока всегда возвращаем Ok
        * */
        if(StringUtils.isEmpty(createTagDTO.getColor()) || !createTagDTO.getColor().matches("^#[A-z]{6}$")){
            createTagDTO.setColor(flatColorsLocal.color());
        }

        CommonTagTransfer tagTransfer;
        try {

            tagTransfer = tagsCache.addTag(createTagDTO.getName(), createTagDTO.getDescription(), createTagDTO.getColor(),createTagDTO.getUser_id()).get(5, TimeUnit.SECONDS);

        }catch (InterruptedException | ExecutionException e) {
            return serverError();
        }catch (TimeoutException e){
            return timeout();
        }
        if(tagTransfer == null){
            return serverError();
        }

        return accepted(tagTransfer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTag(@PathVariable("id") long id) throws InterruptedException, ExecutionException, TimeoutException {

        CommonTagTransfer tag = tagsCache.getTagByID(id).get(5, TimeUnit.SECONDS);
        if(tag == null){
            return notFound();
        }

        return found(tag);
    }
}
