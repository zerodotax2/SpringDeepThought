package ru.projects.prog_ja.services.rest.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.view.IDDto;
import ru.projects.prog_ja.dto.view.RemoveDTO;
import ru.projects.prog_ja.dto.view.create.CreateAnswerDTO;
import ru.projects.prog_ja.dto.view.update.UpdateAnswerDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/questions/answer")
public class RestQuestionAnswerService extends AbstractRestService {

    private final QuestionWriteService questionWriteService;

    @Autowired
    public RestQuestionAnswerService(QuestionWriteService questionWriteService) {
        this.questionWriteService = questionWriteService;
    }

    @PostMapping
    public ResponseEntity<?> addAnswer(@Valid @RequestBody CreateAnswerDTO createAnswerDT0, BindingResult bindingResult,
                                       @SessionAttribute("user")UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        CommonAnswerTransfer answerTransfer = questionWriteService.addAnswer(createAnswerDT0.getId(), createAnswerDT0.getHtmlContent(),
                userDTO.getId());
        if(answerTransfer == null)
            return serverError();


        return accepted(answerTransfer);
    }

    @PutMapping
    public ResponseEntity<?> updateAnswer(@Valid @RequestBody UpdateAnswerDTO updateAnswerDTO, BindingResult bindingResult,
                                          @SessionAttribute("user") UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(questionWriteService.updateAnswer(updateAnswerDTO.getId(),
                updateAnswerDTO.getHtmlContent(),
                userDTO.getId())){
            return ok();
        }

        return  serverError();
    }

    @DeleteMapping
    public ResponseEntity<?> removeAnswer(@Valid @RequestBody RemoveDTO removeDTO, BindingResult bindingResult,
                                          @SessionAttribute("user") UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();


        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(questionWriteService.deleteAnswer(removeDTO.getId(), userDTO.getId()))
            return ok();

        return serverError();
    }

    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public ResponseEntity<?> updateAnswerRating(@Valid @RequestBody UpdateRatingDTO updateRatingDTO,
                                                BindingResult bindingResult,
                                                @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(questionWriteService.updateAnswerRate(updateRatingDTO.getId(), updateRatingDTO.getRate(),
                userDTO.getId())){
            return ok();
        }

        return serverError();
    }

    @RequestMapping(value = "/right", method = RequestMethod.POST)
    public ResponseEntity<?> setRightAnswer(@Valid @RequestBody IDDto idDto, BindingResult bindingResult,
                                            @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(questionWriteService.setRightAnswer(idDto.getId(), userDTO.getId()))
            return ok();

        return serverError();
    }
}
