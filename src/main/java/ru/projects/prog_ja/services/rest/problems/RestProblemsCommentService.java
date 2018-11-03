package ru.projects.prog_ja.services.rest.problems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.view.FeedbackDTO;
import ru.projects.prog_ja.dto.view.RemoveDTO;
import ru.projects.prog_ja.dto.view.create.CreateCommentDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/services/problems/comments")
public class RestProblemsCommentService extends AbstractRestService {

    private final ProblemReadService problemReadService;
    private final ProblemWriteService problemWriteService;

    @Autowired
    public RestProblemsCommentService(ProblemReadService problemReadService,
                                      ProblemWriteService problemWriteService){
        this.problemReadService = problemReadService;
        this.problemWriteService = problemWriteService;
    }

    @PostMapping
    public ResponseEntity<?> postComment(@Valid @RequestBody CreateCommentDTO createCommentDTO, BindingResult bindingResult,
                                         @SessionAttribute UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }

        CommonCommentTransfer comment =  problemWriteService.addComment(createCommentDTO.getId(), createCommentDTO.getText(), userDTO.getId());
        if(comment == null){
            return serverError();
        }
        return found(comment);
    }

    @DeleteMapping
    public ResponseEntity<?> removeComment(@Valid @RequestBody RemoveDTO removeDTO, BindingResult bindingResult,
                                           @SessionAttribute UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(problemWriteService.removeComment(removeDTO.getId(), userDTO.getId())){
            return found(removeDTO);
        }
        return serverError();
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@Valid @RequestBody CreateCommentDTO createCommentDTO, BindingResult bindingResult,
                                           @SessionAttribute UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(problemWriteService.updateComment(createCommentDTO.getId(), createCommentDTO.getText(), userDTO.getId())){
            return found(createCommentDTO);
        }
        return serverError();
    }

    @PostMapping("/rating")
    public ResponseEntity<?> updateRating(@Valid @RequestBody UpdateRatingDTO updateRatingDTO, BindingResult bindingResult,
                                          @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(problemWriteService.updateCommentRate(updateRatingDTO.getId(), updateRatingDTO.getRate(),
                 userDTO.getId()))
            return ok();


        return serverError();
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> sendFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult bindingResult,
                                          @SessionAttribute("user") UserDTO userDTO){
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
