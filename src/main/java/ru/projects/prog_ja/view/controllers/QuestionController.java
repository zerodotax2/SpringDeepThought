package ru.projects.prog_ja.view.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.exceptions.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.dto.view.create.CreateQuestionDTO;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    public static final String CREATE_QUESTION_DTO_NAME = "createQuestionDTO";
    public static final String QUESTIONS_LIST_NAME = "questions";
    public static final String QUESTION_EDIT_NAME = "questionEdit";

    private final QuestionReadService questionReadService;

    public QuestionController(@Autowired QuestionReadService questionReadService){
        this.questionReadService = questionReadService;
    }

    /*
    * Если это GET запрос, то просто отображаем посты
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                     @RequestParam(name = "sort", required = false, defaultValue = "1") String sort) throws InternalServerException {

        List<SmallQuestionTransfer> questions = questionReadService.getSmallQuestions(0, query, type, sort);
        if(questions == null){
            throw new InternalServerException();
        }

        return new ModelAndView("questions/questions", QUESTIONS_LIST_NAME, questions);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getQuestion(@PathVariable("id") long id) throws BadRequestException, NotFoundException{

        FullQuestionTransfer questionTransfer = questionReadService.getOneQuestion(id);
        if(questionTransfer == null){
            throw  new NotFoundException();
        }

        return new ModelAndView("questions/question", "question", questionTransfer);
    }

    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public ModelAndView askQuestion(){

        return new ModelAndView("questions/ask");
    }

    @RequestMapping(value = "/{id}/edit")
    public ModelAndView editQuestion(@PathVariable("id") long id,
                                     @SessionAttribute("user") UserDTO userDTO) throws AccessDeniedException, NotFoundException, NonAuthorizedException {

        if(userDTO == null || userDTO.getId() == -1){
            throw new NonAuthorizedException();
        }

        FullQuestionTransfer fullQuestionTransfer = questionReadService.getOneQuestion(id);
        if(fullQuestionTransfer == null){
            throw new NotFoundException();
        }

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == fullQuestionTransfer.getUser().getId()){
            return new ModelAndView("questions/questionEdit", QUESTION_EDIT_NAME, fullQuestionTransfer);
        }else{

            throw new AccessDeniedException();
        }
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

//        questionReadService.createQuestion(createQuestionDTO.getTitle(), tagsIDs, createQuestionDTO.getHtmlContent(), user.getId());

        return new ModelAndView("redirect:/questions");
    }
}
