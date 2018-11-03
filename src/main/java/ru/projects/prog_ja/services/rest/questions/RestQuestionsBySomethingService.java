package ru.projects.prog_ja.services.rest.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.List;

@RestController
@RequestMapping("/services/questions")
public class RestQuestionsBySomethingService extends AbstractRestService {

    private final QuestionReadService questionReadService;

    @Autowired
    public RestQuestionsBySomethingService(QuestionReadService questionReadService){
        this.questionReadService = questionReadService;
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getQuestionsByTag(@PathVariable("id") long id,
                                               @RequestParam(value = "size", defaultValue = "6") String size){


        BySomethingContainer container = questionReadService
                .getQuestionsByTag(0,size,id, "rating", "1");
        if(container == null){
            return serverError();
        }

        return found(container);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuestionsByUser(@PathVariable("id") long id,
                                                @RequestParam(value = "size", defaultValue = "6") String size){


        BySomethingContainer container = questionReadService.getQuestionsByUser(0, size,id, "rating", "1");
        if(container == null){
            return notFound();
        }


        return found(container);
    }

    @RequestMapping(value = "/user/answers/{id}" ,method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnswersBuyUser(@PathVariable("id") long id,
                                               @RequestParam(value = "size", defaultValue = "6") String size){

        BySomethingContainer container = questionReadService
                .getAnswersByUser(0, size,id, "rating", "1");
        if(container == null){
            return notFound();
        }

        return found(container);
    }


}
