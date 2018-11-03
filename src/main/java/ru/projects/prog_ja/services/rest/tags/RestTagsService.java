package ru.projects.prog_ja.services.rest.tags;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.create.CreateTagDTO;
import ru.projects.prog_ja.dto.view.update.UpdateTagDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

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

        List<SmallTagTransfer> popularTags = tagsReadService.getPopularTags();
        if(popularTags == null){
            return serverError();
        }

        return found(popularTags);
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "q", required = false) String q,
                                      @RequestParam(value = "sort", defaultValue = "1") String sort,
                                      @RequestParam(value = "start") String start,
                                      @RequestParam(value = "type", defaultValue = "rating") String type){

        if(start == null || start.equals(""))

            return badRequest();
        else if(!start.matches("^\\d+$") || start.length() > 32){

            return incorrectFormat();
        }else{
            List<CommonTagTransfer> tags = tagsReadService.getTags(Integer.parseInt(start), q, type, sort);
            if(tags == null){
                return serverError();
            }

            return found(tags);
        }
    }

    /*
    * Создаём тег с переданными параметрами
    * */
    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagDTO createTagDTO, BindingResult bindingResult,
                                       @SessionAttribute("user") UserDTO userDTO){


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
                                       @SessionAttribute("user") UserDTO userDTO){


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
}
