package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

@Controller
@RequestMapping("/facts")
public class FactsController {

    private static final String FACT_LIST_NAME = "facts";
    private static final String FACT_EDIT_NAME = "factEdit";
    private static final String PAGES_NAME = "pages";

    private final FactsReadService factsReadService;
    private final TagsReadService tagsReadService;
    private  final UserReadService userReadService;

    @Autowired
    public FactsController(FactsReadService factsReadService,
                           TagsReadService tagsReadService,
                           UserReadService userReadService) {
        this.factsReadService = factsReadService;
        this.tagsReadService = tagsReadService;
        this.userReadService = userReadService;
    }

    @GetMapping
    public ModelAndView getFacts(@RequestParam(name = "q", required = false) String query,
                                 @RequestParam(name = "sort", required = false) String sort,
                                 @RequestParam(name = "type", required = false) String type,
                                 @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container = factsReadService.getFacts(page, query, type, sort);

        ModelAndView model = new ModelAndView("facts/facts");
        model.addObject(FACT_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @GetMapping("/user/{id}")
    public ModelAndView getFactsByUser(@PathVariable("id") long id ,
                                       @RequestParam(name = "size", defaultValue = "10") String size,
                                       @RequestParam(name = "q", required = false) String query,
                                       @RequestParam(name = "sort", required = false) String sort,
                                       @RequestParam(name = "type", required = false) String type,
                                       @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container
                = factsReadService.getFactsByUser(page,size,id, query, type, sort);

        ModelAndView model = new ModelAndView("facts/facts");
        model.addObject("specialViews", new SpecialView(userReadService.getUsername(id),
                "/services/facts/user/"+id));
        model.addObject(FACT_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @GetMapping("/tag/{id}")
    public ModelAndView getFactsByTag(@PathVariable("id") long id ,
                                       @RequestParam(name = "size", defaultValue = "10") String size,
                                       @RequestParam(name = "q", required = false) String query,
                                       @RequestParam(name = "sort", required = false) String sort,
                                       @RequestParam(name = "type", required = false) String type,
                                      @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container
                = factsReadService.getFactsByTag(page,size,id, query, type, sort);

        ModelAndView model = new ModelAndView("facts/facts");
        model.addObject("specialViews", new SpecialView(tagsReadService.getName(id),
                "/services/facts/tag/"+id));
        model.addObject(FACT_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());
        return model;
    }

    @GetMapping("/add")
    public ModelAndView addFact(){

        return new ModelAndView("facts/factAdd");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editFact(@PathVariable("id") long id,
                                 @SessionAttribute(name = "user", required = false)UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
        if(userDTO == null || userDTO.getId() == -1)
            throw new NonAuthorizedException();

        FullFactTransfer factTransfer = factsReadService.getFullFact(id);
        if(factTransfer == null)
            throw new NotFoundException();

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == factTransfer.getUser().getId()){

            return new ModelAndView("facts/factEdit", FACT_EDIT_NAME, factTransfer);
        }else{

            throw new AccessDeniedException();
        }
    }
}
