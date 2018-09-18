package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.interfaces.FactsService;
import ru.projects.prog_ja.logic.services.interfaces.QuestionService;
import ru.projects.prog_ja.dto.view.create.CreateQuestionDTO;
import ru.projects.prog_ja.view.exceptions.BadRequestException;
import ru.projects.prog_ja.view.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    public static final String CREATE_QUESTION_DTO_NAME = "createQuestionDTO";
    public static final String QUESTIONS_LIST_NAME = "questionsList";

    private final QuestionService questionService;
    private final FactsService factsService;

    public QuestionController(@Autowired QuestionService questionService,
                              @Autowired FactsService factsService){
        this.questionService = questionService;
        this.factsService = factsService;
    }

    /*
    * Если это GET запрос, то просто отображаем посты
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                     @RequestParam(name = "sort", required = false, defaultValue = "desc") int sort){

        ModelAndView model = new ModelAndView();

        List<SmallQuestionTransfer> questions;
        if(!StringUtils.isEmpty(query)){
            questions = questionService.findSmallQuestions(0, query, type, sort);
        }else {
            questions = questionService.getSmallQuestions(0, type, sort);
        }

        model.addObject(QUESTIONS_LIST_NAME, questions);
        model.addObject(MainController.FACT_NAME, factsService.getRandomFact());
        model.setViewName("questions/questions");

        return model;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getQuestion(@PathVariable("id") String strID) throws BadRequestException, NotFoundException{

        if(!strID.matches("^\\d+$") || strID.equals("")){
            throw new BadRequestException();
        }

        FullQuestionTransfer questionTransfer = questionService.getOneQuestion(Long.parseLong(strID));
        if(questionTransfer == null){
            throw  new NotFoundException();
        }

        return new ModelAndView("questions/question", "question", questionTransfer);
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public ModelAndView writeQuestion(){

        return new ModelAndView("questions/write",CREATE_QUESTION_DTO_NAME, new CreateQuestionDTO());
    }

    @Deprecated
//    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public ModelAndView createQuestion(@Valid @ModelAttribute(CREATE_QUESTION_DTO_NAME) CreateQuestionDTO createQuestionDTO, BindingResult bindingResult,
                                       @SessionAttribute("user") SmallUserTransfer user){

        if(bindingResult.hasErrors()){
            return new ModelAndView("questions/write");
        }

        if(createQuestionDTO.getUserId() != user.getId()){
            return new ModelAndView("questions/write");
        }

        List<Long> tagsIDs = new ArrayList<>();
        String[] tags = createQuestionDTO.getTagsSTR().split(" ");
        for(String tag : tags){
            if(!tag.matches("^\\d+$") || tag.length() > 64)
                return new ModelAndView("questions/write");

            tagsIDs.add(Long.parseLong(tag));
        }

        questionService.createQuestion(createQuestionDTO.getTitle(), tagsIDs, createQuestionDTO.getHtmlContent(), user.getId());

        return new ModelAndView("redirect:/questions");
    }
}
