package ru.projects.prog_ja.services.rest.problems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullProblemTransfer;
import ru.projects.prog_ja.dto.view.CheckDTO;
import ru.projects.prog_ja.dto.view.CheckResult;
import ru.projects.prog_ja.dto.view.FeedbackDTO;
import ru.projects.prog_ja.dto.view.create.CreateProblemDTO;
import ru.projects.prog_ja.dto.view.update.UpdateProblemDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.exceptions.AlreadyExistException;
import ru.projects.prog_ja.exceptions.RepeatVotedException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

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
    public ResponseEntity<?> getProblems(@RequestParam(name = "q", required = false) String q,
                                          @RequestParam(name = "sort", defaultValue = "1") String sort,
                                          @RequestParam(name = "page", defaultValue = "1") String page,
                                          @RequestParam(name = "type", defaultValue = "rating") String type,
                                         @RequestParam(name = "difficult", required = false) String difficult){

        return found(problemReadService.getProblems(page,difficult,
                q, type, sort));

    }
    

    @PostMapping
    public ResponseEntity<?> createProblem(@Valid @RequestBody CreateProblemDTO createProblemDTO, BindingResult bindingResult,
                                           @SessionAttribute(name = "user", required = false)UserDTO userDTO){

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
                                           @SessionAttribute(name = "user", required = false) UserDTO userDTO){

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
                                        @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }

        try{
            if(problemWriteService.changeRate(updateRatingDTO.getId(), updateRatingDTO.getRate(), userDTO.getId())){
                return ok();
            }
        }catch (RepeatVotedException e){
            return already();
        }

        return serverError();
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkAnswer(@Valid @RequestBody CheckDTO checkDTO, BindingResult bindingResult,
                                         @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(bindingResult.hasErrors()){
            return badRequest();
        }

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }

        boolean right = false;

        try{
           right = problemWriteService.checkAnswer(checkDTO.getId() ,checkDTO.getValue(), userDTO.getId());
        }catch (AlreadyExistException e) {
            return already();
        }

        return found(new CheckResult(right));
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> sendFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult bindingResult,
                                          @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(problemWriteService.sendFeedback(feedbackDTO.getId(),feedbackDTO.getText(), userDTO.getId())){
            return ok();
        }

        return serverError();
    }
}
