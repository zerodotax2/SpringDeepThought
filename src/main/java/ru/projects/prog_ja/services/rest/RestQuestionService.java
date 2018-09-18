package ru.projects.prog_ja.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.view.RenderDTO;
import ru.projects.prog_ja.dto.view.SearchDTO;
import ru.projects.prog_ja.logic.services.interfaces.QuestionService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/questions")
public class RestQuestionService extends AbstractRestService {

    private final QuestionService questionService;

    public RestQuestionService (@Autowired QuestionService questionService){
        this.questionService = questionService;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuestionsByUser(@PathVariable("id") long id){

        List<SmallQuestionTransfer> questions = questionService.getQuestionsByUser(id);
        if(questions == null){
            return  serverError();
        }

        if(questions.size() == 0){
            return  notFound();
        }

        return found(questions);
    }

    @GetMapping
    public ResponseEntity<?> getQuestions(@Valid @RequestBody RenderDTO renderDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List questions;

        switch (renderDTO.getObjectSize()) {
            case "small":
                questions = questionService.getSmallQuestions(renderDTO.getStart(),
                        renderDTO.getType(), renderDTO.getSort());
                break;
            case "common":
                questions = questionService.getQuestions(renderDTO.getStart(), renderDTO.getType(), renderDTO.getSort());
                break;
            default:
                return badRequest();
        }

        if(questions == null){
            return serverError();
        }

        return found(questions);

    }

    @GetMapping("/find")
    public ResponseEntity<?> findQuestions(@Valid @RequestBody SearchDTO searchDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List questions;

        switch (searchDTO.getObjectSize()) {
            case "small":

                questions = questionService.findSmallQuestions(searchDTO.getStart(),
                        searchDTO.getSearch(), searchDTO.getType(), searchDTO.getSort());
                break;
            case "common":

                questions = questionService.findCommonQuestions(searchDTO.getStart(), searchDTO.getSearch(),
                        searchDTO.getType(), searchDTO.getSort());
                break;
            default:
                return badRequest();
        }

        if(questions == null){
            return serverError();
        }

        return found(questions);
    }

}
