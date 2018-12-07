package ru.projects.prog_ja.services.rest.tags;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.view.create.CreateTagDTO;
import ru.projects.prog_ja.dto.view.update.UpdateTagDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/services/tags")
public class RestTagsService extends AbstractRestService {

    private final TagsReadService tagsReadService;
    private final TagsWriteService tagsWriteService;

    @Autowired
    public RestTagsService(TagsReadService tagsReadService,
                           TagsWriteService tagsWriteService){
        this.tagsReadService = tagsReadService;
        this.tagsWriteService = tagsWriteService;
    }


    /*
    * Возвращаем по get запросу JSON лист популярных тегов
    * */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularTags(){

        return found(tagsReadService.getPopularTags());
    }

    @GetMapping
    public ResponseEntity<?> getTags(@RequestParam(name = "q", required = false) String q,
                                      @RequestParam(name = "sort", defaultValue = "1") String sort,
                                      @RequestParam(name = "start") String start,
                                      @RequestParam(name = "type", defaultValue = "rating") String type,
                                      @RequestParam(name = "page", defaultValue = "1") String page){

            return found(tagsReadService.getTags(page, q, type, sort));
    }

    /*
    * Создаём тег с переданными параметрами
    * */
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagDTO createTagDTO, BindingResult bindingResult,
                                       @SessionAttribute(name = "user", required = false) UserDTO userDTO){


        if(bindingResult.hasErrors()){
            return badRequest();
        }
        /*
        * Проверяем, что юзер тот за кого себя выдаёт
        * */
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        CommonTagTransfer tagTransfer = tagsWriteService.createTag(createTagDTO.getName(), createTagDTO.getDescription(), createTagDTO.getColor(), userDTO.getId());

        if(tagTransfer == null){
            return serverError();
        }

        return accepted(tagTransfer);
    }

    @PutMapping
    public ResponseEntity<?> updateTag(@Valid @RequestBody UpdateTagDTO updateTagDTO, BindingResult bindingResult,
                                       @SessionAttribute(name = "user", required = false) UserDTO userDTO){


        if(bindingResult.hasErrors()){
            return badRequest();
        }
        /*
        * Проверяем, что юзер тот за кого себя выдаёт
        * */
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(tagsWriteService.updateTag(updateTagDTO.getTagId() ,updateTagDTO.getName(), updateTagDTO.getDescription(), updateTagDTO.getColor(), userDTO.getId())){
            return ok();
        }else
            return serverError();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTag(@PathVariable("id") long id) {

        CommonTagTransfer tag = tagsReadService.getCommonTag(id);
        if(tag == null){
            return notFound();
        }

        return found(tag);
    }

    @GetMapping("/small")
    public ResponseEntity<?> getTagsByPrefix(@RequestParam(name = "q", defaultValue = "") String q){

        return !"".equals(q) ?found(tagsReadService.getTagsByPrefix(q)) : badRequest();
    }
}
