package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.exceptions.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagsController {

    private final TagsReadService tagsReadService;

    private final static String TAGS_LIST_NAME = "tags";
    private final static String TAG_DTO_NAME = "tag";
    private final static String TAG_EDIT_NAME = "tagEdit";

    public TagsController(@Autowired TagsReadService tagsReadService) {
        this.tagsReadService = tagsReadService;
    }

    @GetMapping
    public ModelAndView getTags(@RequestParam(name = "q", required = false) String query,
                                @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                @RequestParam(name = "sort", required = false, defaultValue = "1") String sort) throws InternalServerException {

        List<CommonTagTransfer> commonTagTransfers = tagsReadService.getTags(0, query, type, sort);
        if(commonTagTransfers == null){
            throw new InternalServerException();
        }

        return new ModelAndView("tags/tags", TAGS_LIST_NAME, commonTagTransfers);
    }

    @GetMapping("/{id}")
    public ModelAndView getFullTag(@PathVariable("id") long id) throws BadRequestException, InternalServerException, NotFoundException {

        FullTagTransfer fullTagTransfer =
                tagsReadService.getFullTag(id);
        if(fullTagTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("tags/tag", TAG_DTO_NAME, fullTagTransfer);
    }

    @GetMapping("/add")
    public ModelAndView createTag(){

        return new ModelAndView("tags/tagAdd");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editTag(@PathVariable("id") long id,
                                @SessionAttribute("user")UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
        if(userDTO == null)
            throw new NonAuthorizedException();

        FullTagTransfer fullTagTransfer = tagsReadService.getFullTag(id);
        if(fullTagTransfer == null)
            throw new NotFoundException();

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == fullTagTransfer.getUser().getId()) {

            return new ModelAndView("tags/tagEdit", TAG_EDIT_NAME, fullTagTransfer);
        }else{

            throw new AccessDeniedException();
        }
    }
}
