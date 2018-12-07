package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.exceptions.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

@Controller
@RequestMapping("/tags")
public class TagsController {

    private final TagsReadService tagsReadService;
    private final UserReadService userReadService;

    private final static String TAGS_LIST_NAME = "tags";
    private final static String TAG_DTO_NAME = "fullTagDTO";
    private final static String TAG_EDIT_NAME = "tagEdit";
    private final static String PAGES_NAME = "pages";

    @Autowired
    public TagsController(TagsReadService tagsReadService,
                          UserReadService userReadService) {
        this.tagsReadService = tagsReadService;
        this.userReadService = userReadService;
    }

    @GetMapping
    public ModelAndView getTags(@RequestParam(name = "q", required = false) String query,
                                @RequestParam(name = "type", required = false) String type,
                                @RequestParam(name = "sort", required = false) String sort,
                                @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container =
                tagsReadService.getTags(page, query, type, sort);

        ModelAndView model = new ModelAndView("tags/tags");
        model.addObject(TAGS_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;


    }

    @GetMapping("/user/{id}")
    public ModelAndView getTagsByUser(@RequestParam(name = "q", required = false) String query,
                                @RequestParam(name = "type", required = false) String type,
                                @RequestParam(name = "sort", required = false) String sort,
                                @RequestParam(name = "page", required = false) String page,
                                @PathVariable("id") long userId) throws InternalServerException {

        PageableContainer container =
                tagsReadService.getTagsByUser(page, "10", userId, query, type, sort);

        ModelAndView model = new ModelAndView("tags/tags");
        model.addObject(TAGS_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());
        model.addObject("specialView", new SpecialView(userReadService.getUsername(userId),
                "/services/tags/user/" + userId));

        return model;
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
                                @SessionAttribute(name = "user", required = false)UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
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
