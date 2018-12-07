package ru.projects.prog_ja.services.rest.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.services.AbstractRestService;

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
                                               @RequestParam(name = "size", defaultValue = "6") String size,
                                               @RequestParam(name = "q", required = false) String q,
                                               @RequestParam(name = "type", defaultValue = "rating") String type,
                                               @RequestParam(name = "sort", defaultValue = "1")String sort,
                                               @RequestParam(name = "page", defaultValue = "1") String page){

        return found(questionReadService
                .getQuestionsByTag(page,size,id, q, type,sort));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuestionsByUser(@PathVariable("id") long id,
                                                @RequestParam(name = "size", defaultValue = "6") String size,
                                                @RequestParam(name = "q", required = false) String q,
                                                @RequestParam(name = "type", defaultValue = "rating") String type,
                                                @RequestParam(name = "sort", defaultValue = "1") String sort,
                                                @RequestParam(name = "page", defaultValue = "1") String page){

        return found(questionReadService.getQuestionsByUser(page, size,id, q, type, sort));
    }

    @RequestMapping(value = "/user/answers/{id}" ,method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnswersByUser(@PathVariable("id") long id,
                                               @RequestParam(name = "size", defaultValue = "6") String size,
                                               @RequestParam(name = "q", required = false) String q,
                                               @RequestParam(name = "type", defaultValue = "rating") String type,
                                               @RequestParam(name = "sort", defaultValue = "1")String sort){
        return found( questionReadService
                .getAnswersByUser(0, size,id, q, type, sort));
    }


}
