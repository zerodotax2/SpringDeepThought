package ru.projects.prog_ja.services.rest.problems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.view.CheckDTO;
import ru.projects.prog_ja.dto.view.CheckResult;
import ru.projects.prog_ja.dto.view.create.CreateProblemDTO;
import ru.projects.prog_ja.dto.view.update.UpdateProblemDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/problems")
public class RestProblemsService extends AbstractRestService {

    private final ProblemReadService problemReadService;
    private final ProblemWriteService problemWriteService;

    @Autowired
    public RestProblemsService(ProblemReadService problemReadService,
                               ProblemWriteService problemWriteService){
        this.problemReadService = problemReadService;
        this.problemWriteService = problemWriteService;
    }



    @GetMapping
    public ResponseEntity<?> getProblems(@RequestParam(value = "q", required = false) String q,
                                          @RequestParam(value = "sort", defaultValue = "1") String sort,
                                          @RequestParam(value = "start") String start,
                                          @RequestParam(value = "type", defaultValue = "rating") String type,
                                         @RequestParam(value = "difficult", required = false) String difficult){

        if(start == null || start.equals(""))
            return badRequest();
        else if(!start.matches("^\\d+&") || start.length() > 32)
            return incorrectFormat();

        List<SmallProblemTransfer> questions = problemReadService.getProblems(Integer.parseInt(start),difficult,
                q, type, sort);
        if(questions == null || questions.size() == 0){
            return notFound();
        }

        return found(questions);

    }
    

    @PostMapping
    public ResponseEntity<?> createProblem(@Valid @RequestBody CreateProblemDTO createProblemDTO, BindingResult bindingResult,
                                           @SessionAttribute("user")UserDTO userDTO){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        
        FullProblemTransfer fullProblemTransfer = problemWriteService.createProblem(
                userDTO.getId(), createProblemDTO.getTitle(), createProblemDTO.getProblemContent(), createProblemDTO.getSolutionContent(),
                createProblemDTO.getAnswer(), createProblemDTO.getDifficult(), createProblemDTO.getTags());
        if(fullProblemTransfer == null){
            return serverError();
        }

        return found(fullProblemTransfer);
    }
    
    @PutMapping
    public ResponseEntity<?> updateProblem(@Valid @RequestBody UpdateProblemDTO updateProblemDTO, BindingResult bindingResult,
                                           @SessionAttribute("user")UserDTO userDTO){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        
        if(problemWriteService.updateProblem( updateProblemDTO.getProblemId(),
                updateProblemDTO.getTitle(), updateProblemDTO.getProblemContent(), updateProblemDTO.getSolutionContent(),
                updateProblemDTO.getAnswer(), updateProblemDTO.getDifficult(), updateProblemDTO.getTags(), userDTO.getId())){
            return ok();
        }else{

            return serverError();
        }
    }

    @PostMapping("/rating")
    public ResponseEntity<?> updateRate(@Valid @RequestBody UpdateRatingDTO updateRatingDTO,
                                        BindingResult bindingResult,
                                        @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }

        if(problemWriteService.changeRate(updateRatingDTO.getId(), updateRatingDTO.getRate(), userDTO.getId())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkAnswer(@Valid @RequestBody CheckDTO checkDTO, BindingResult bindingResult,
                                         @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }

        return found(new CheckResult(
                problemWriteService.checkAnswer(checkDTO.getId() ,checkDTO.getValue(), userDTO.getId())));
    }
}
