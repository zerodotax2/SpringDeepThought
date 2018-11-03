package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.full.FullSolutionTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.exceptions.NotFoundException;

@Controller
@RequestMapping("/problems/{id}/solution")
public class SolutionController {

    private final ProblemReadService problemReadService;

    private static final String SOLUTION_DTO_NAME = "solution";

    public SolutionController(@Autowired ProblemReadService problemReadService) {
        this.problemReadService = problemReadService;
    }

    @GetMapping
    public ModelAndView getSolution(@PathVariable(value = "id") long id) throws BadRequestException, NotFoundException {

        FullSolutionTransfer fullSolutionTransfer =
                problemReadService.getProblemSolution(id);

        if(fullSolutionTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("problems/solution",
                SOLUTION_DTO_NAME, fullSolutionTransfer);
    }
}
