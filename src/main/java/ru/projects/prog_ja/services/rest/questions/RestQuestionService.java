package ru.projects.prog_ja.services.rest.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.create.CreateQuestionDTO;
import ru.projects.prog_ja.dto.view.update.UpdateQuestionDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

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
                                            @SessionAttribute("user")UserDTO userDTO){

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
                                            @SessionAttribute("user") UserDTO userDTO){

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
    public ResponseEntity<?> getQuestions(@RequestParam(value = "q", required = false) String q,
                                          @RequestParam(value = "sort", defaultValue = "1") String sort,
                                          @RequestParam(value = "start") String start,
                                          @RequestParam(value = "type", defaultValue = "rating") String type){

        if(start == null || start.equals(""))
            return badRequest();
        else if(!start.matches("^\\d+&") || start.length() > 32)
            return incorrectFormat();

        List<SmallQuestionTransfer> questions = questionReadService.getSmallQuestions(Integer.parseInt(start),
                q, type, sort);
        if(questions == null || questions.size() == 0){
            return notFound();
        }

        return found(questions);

    }
    
}
