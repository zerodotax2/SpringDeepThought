package ru.projects.prog_ja.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.view.RenderDTO;
import ru.projects.prog_ja.dto.view.SearchDTO;
import ru.projects.prog_ja.dto.view.create.CreateProblemDTO;
import ru.projects.prog_ja.logic.services.interfaces.ProblemService;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/problems")
public class RestProblemsService extends AbstractRestService {

    private final ProblemService problemService;
    private final UserService userService;

    public RestProblemsService(@Autowired ProblemService problemService,
                               @Autowired UserService userService){
        this.problemService = problemService;
        this.userService = userService;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProblemsByUser(@PathVariable("id") long id){

        List<SmallProblemTransfer> problems = problemService.getProblemsByUser(id);
        if(problems == null){
           return  serverError();
        }

        if(problems.size() == 0){
           return  notFound();
        }

        return found(problems);
    }

    @GetMapping
    public ResponseEntity<?> getProblems(@Valid @RequestBody RenderDTO renderDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }


        List problems;
        switch (renderDTO.getObjectSize()){
            case "small":
                problems = problemService.getSmallProblems(renderDTO.getStart(), renderDTO.getType(), renderDTO.getSort());
                break;

             default: return badRequest();
        }

        if(problems == null){
            return serverError();
        }

        return found(problems);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findProblems(@Valid @RequestBody SearchDTO searchDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List problems;
        switch (searchDTO.getObjectSize()){
            case "small":
                problems = problemService.findSmallProblems(searchDTO.getStart(), searchDTO.getSearch(),
                        searchDTO.getType(), searchDTO.getSort());
                break;

            default: return badRequest();
        }

        if(problems == null){
            return badRequest();
        }

        return found(problems);
    }

    @PostMapping
    public ResponseEntity<?> createProblem(@Valid @RequestBody CreateProblemDTO createProblemDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        FullProblemTransfer fullProblemTransfer = null;

        return found(fullProblemTransfer);
    }
}
