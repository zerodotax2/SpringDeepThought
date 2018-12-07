package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.view.EditProblemDTO;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

@Controller
@RequestMapping("/problems")
public class ProblemController {

    private static final String PROBLEMS_DTO_NAME = "problems";
    private static final String PROBLEM_DTO_NAME = "fullProblemDTO";
    private static final String PROBLEM_EDIT_NAME = "problemEdit";
    private static final String PAGES_NAME = "pages";

    private final ProblemReadService problemReadService;
    private final TagsReadService tagsReadService;
    private final UserReadService userReadService;

    @Autowired
    public ProblemController( ProblemReadService problemReadService,
                              TagsReadService tagsReadService,
                              UserReadService userReadService) {
        this.problemReadService = problemReadService;
        this.tagsReadService = tagsReadService;
        this.userReadService = userReadService;
    }

    @GetMapping
    public ModelAndView getProblems(@RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false) String type,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "difficult", required = false) String difficult,
                                    @RequestParam(name = "page", required = false) String page) throws NotFoundException {

        PageableContainer container
                = problemReadService.getProblems(page, query, difficult, type, sort);

        ModelAndView model = new ModelAndView("problems/problems");
        model.addObject(PROBLEMS_DTO_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @GetMapping("/tag/{id}")
    public ModelAndView getProblemsByTag(@PathVariable("id") long id,
                                    @RequestParam(name = "size", defaultValue = "10") String size,
                                    @RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false) String type,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "difficult", required = false) String difficult,
                                    @RequestParam(name = "page", required = false   ) String page)throws InternalServerException {

        PageableContainer container =
                problemReadService.getProblemsByTag(page, size, id, difficult, query, type, sort);
        ModelAndView model = new ModelAndView("problems/problems");
        model.addObject("specialView", new SpecialView(tagsReadService.getName(id),
                "/services/problems/tag/"+id));
        model.addObject(PROBLEMS_DTO_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @GetMapping("/user/{id}")
    public ModelAndView getProblemsByUser(@PathVariable("id") long id,
                                    @RequestParam(name = "size", defaultValue = "10") String size,
                                    @RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                    @RequestParam(name = "sort", required = false, defaultValue = "1") String sort,
                                    @RequestParam(name = "difficult", required = false) String difficult,
                                    @RequestParam(name = "page", defaultValue = "1") String page)throws InternalServerException { ;

        PageableContainer container =
                problemReadService.getProblemsByUser(page, size, id, difficult, query, type, sort);
        ModelAndView model = new ModelAndView("problems/problems");
        model.addObject("specialView", new SpecialView(userReadService.getUsername(id),
                "/services/problems/user/"+id));
        model.addObject(PROBLEMS_DTO_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @GetMapping("/{id}")
    public ModelAndView getOneProblem(@PathVariable("id") long id,
                                      @SessionAttribute(name = "user", required = false) UserDTO userDTO) throws NotFoundException {

        FullProblemTransfer fullProblemTransfer = problemReadService.getFullProblem(id, userDTO);
        if(fullProblemTransfer == null){
            throw  new NotFoundException();
        }


        return new ModelAndView("problems/problem",
                PROBLEM_DTO_NAME, fullProblemTransfer);
    }

    @GetMapping("/create")
    public ModelAndView createProblem(){

        return new ModelAndView("problems/problemCreate");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editProblem(@PathVariable("id") long id,
                                    @SessionAttribute(name = "user", required = false) UserDTO userDTO) throws NotFoundException, AccessDeniedException, NonAuthorizedException {

        if(userDTO == null || userDTO.getId() == -1){
            throw new NonAuthorizedException();
        }

        EditProblemDTO problemTransfer = problemReadService.getEditProblem(id);
        if(problemTransfer == null){
            throw new NotFoundException();
        }

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == problemTransfer.getUser().getId()){

            return new ModelAndView("problems/problemEdit", PROBLEM_EDIT_NAME, problemTransfer);
        }else{
            throw new AccessDeniedException();
        }
    }
}
