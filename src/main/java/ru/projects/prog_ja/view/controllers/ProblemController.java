package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;

import java.util.List;

@Controller
@RequestMapping("/problems")
public class ProblemController {

    private static final String PROBLEMS_DTO_NAME = "problems";
    private static final String PROBLEM_DTO_NAME = "problem";
    private static final String PROBLEM_EDIT_NAME = "problemEdit";

    private final ProblemReadService problemReadService;

    @Autowired
    public ProblemController( ProblemReadService problemReadService) {
        this.problemReadService = problemReadService;
    }

    @GetMapping
    public ModelAndView getProblems(@RequestParam(value = "q", required = false) String query,
                                    @RequestParam(value = "type", required = false, defaultValue = "rating") String type,
                                    @RequestParam(value = "sort", required = false, defaultValue = "1") String sort,
                                    @RequestParam(value = "difficult", required = false) String difficult ) throws NotFoundException {

        List<SmallProblemTransfer> problemTransfers = problemReadService.getProblems(0, query, difficult, type, sort);
        if(problemTransfers == null)
            throw new NotFoundException();

        return new ModelAndView("problems/problems", PROBLEMS_DTO_NAME, problemTransfers);
    }

    @GetMapping("/{id}")
    public ModelAndView getOneProblem(@PathVariable("id") long id) throws NotFoundException, BadRequestException {

        FullProblemTransfer fullProblemTransfer = problemReadService.getFullProblem(id);
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
                                    @SessionAttribute("user") UserDTO userDTO) throws NotFoundException, AccessDeniedException, NonAuthorizedException {

        if(userDTO == null || userDTO.getId() == -1){
            throw new NonAuthorizedException();
        }

        FullProblemTransfer problemTransfer = problemReadService.getFullProblem(id);
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
