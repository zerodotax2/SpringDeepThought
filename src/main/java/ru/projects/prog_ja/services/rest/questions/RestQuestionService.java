package ru.projects.prog_ja.services.rest.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.view.create.CreateQuestionDTO;
import ru.projects.prog_ja.dto.view.update.UpdateQuestionDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.exceptions.RepeatVotedException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/questions")
public class RestQuestionService extends AbstractRestService {

    private final QuestionReadService questionReadService;
    private final QuestionWriteService questionWriteService;

    @Autowired
    public RestQuestionService (QuestionReadService questionReadService,
                                QuestionWriteService questionWriteService){
        this.questionReadService = questionReadService;
        this.questionWriteService = questionWriteService;
    }


    @PostMapping
    public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionDTO createQuestionDTO,
                                            BindingResult bindingResult,
                                            @SessionAttribute(name = "user", required = false)UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        FullQuestionTransfer fullQuestionTransfer = questionWriteService.createQuestion(
                createQuestionDTO.getTitle(), createQuestionDTO.getTags(), createQuestionDTO.getHtmlContent(), userDTO.getId());
        if(fullQuestionTransfer == null)
            return serverError();
        
        return accepted(fullQuestionTransfer);
    }
    
    @PutMapping
    public ResponseEntity<?> updateQuestion(@Valid @RequestBody UpdateQuestionDTO updateQuestionDTO,
                                            BindingResult bindingResult,
                                            @SessionAttribute(name = "user", required = false) UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(questionWriteService.updateQuestion( updateQuestionDTO.getQuestionId(),
                updateQuestionDTO.getTitle(),updateQuestionDTO.getHtmlContent(), updateQuestionDTO.getTags(),  userDTO.getId())){
            return ok();
        }

        return serverError();
    }


    @GetMapping
    public ResponseEntity<?> getQuestions(@RequestParam(name = "q", required = false) String q,
                                          @RequestParam(name = "sort", defaultValue = "1") String sort,
                                          @RequestParam(name = "page", defaultValue = "1") String page,
                                          @RequestParam(name = "type", defaultValue = "rating") String type){
        return found(questionReadService.getSmallQuestions(page,
                q, type, sort));

    }

    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public ResponseEntity<?> updateQuestionRating(@Valid @RequestBody UpdateRatingDTO updateRatingDTO,
                                                BindingResult bindingResult,
                                                @SessionAttribute(name = "user", required = false) UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        try{
            if(questionWriteService.updateQuestionRate(updateRatingDTO.getId(), updateRatingDTO.getRate(),
                    userDTO.getId()))
                return ok();
        }catch (RepeatVotedException e){
            return already();
        }


        return serverError();
    }
}
