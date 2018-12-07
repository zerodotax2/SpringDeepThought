package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static final String FULL_USER_NAME="fullUserDTO";
    public static final String USER_LIST_NAME="users";
    public static final String PAGES_NAME="pages";

    private final UserReadService userReadService;
    private final TagsReadService tagsReadService;

    @Autowired
    public UserController( UserReadService userReadService,
                           TagsReadService tagsReadService){
        this.userReadService = userReadService;
        this.tagsReadService = tagsReadService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView user(@PathVariable("id") long id) throws NotFoundException {


        FullUserTransfer fullUserTransfer = userReadService.getFullUser(id);
        if(fullUserTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("users/user", FULL_USER_NAME, fullUserTransfer);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(@RequestParam(name = "q", required = false) String query,
                              @RequestParam(name = "type", required = false) String type,
                              @RequestParam(name = "sort", required = false) String sort,
                              @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container =
                userReadService.getUsers(page, query, type, sort);

        ModelAndView model = new ModelAndView("users/users");
        model.addObject(USER_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
    public ModelAndView getUsersByInterests(@PathVariable("id") long id,
                                            @RequestParam(name = "q", required = false) String query,
                                            @RequestParam(name = "type", required = false) String type,
                                            @RequestParam(name = "sort", required = false) String sort,
                                            @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container
                = userReadService.getUsersByInterests(page, "10", id, query, type,sort);
        ModelAndView model = new ModelAndView("users/user");
        model.addObject("specialView", new SpecialView(tagsReadService.getName(id),
                "/services/users/tag/"+id));
        model.addObject(USER_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return  model;
    }
}

