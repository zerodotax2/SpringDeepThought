package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.dto.view.create.CreateQuestionDTO;
import ru.projects.prog_ja.exceptions.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

import javax.validation.Valid;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    public static final String CREATE_QUESTION_DTO_NAME = "createQuestionDTO";
    public static final String QUESTIONS_LIST_NAME = "questions";
    public static final String QUESTION_EDIT_NAME = "questionEdit";
    public static final String QUESTION_DTO_NAME = "fullQuestionDTO";
    public static final String PAGES_NAME = "pages";

    private final QuestionReadService questionReadService;
    private final TagsReadService tagsReadService;
    private final UserReadService userReadService;

    @Autowired
    public QuestionController(QuestionReadService questionReadService,
                              TagsReadService tagsReadService,
                              UserReadService userReadService){
        this.questionReadService = questionReadService;
        this.tagsReadService = tagsReadService;
        this.userReadService = userReadService;
    }

    /*
    * Если это GET запрос, то просто отображаем посты
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container
                = questionReadService.getSmallQuestions(page, query, type, sort);

        ModelAndView model = new ModelAndView("questions/questions");
        model.addObject(QUESTIONS_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/tag/{id}",method = RequestMethod.GET)
    public ModelAndView getQuestionsByTag(@PathVariable("id") long id,
                                     @RequestParam(name = "size", defaultValue = "10") String size,
                                     @RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container
                = questionReadService.getQuestionsByTag(page, size,id, query, type, sort);

        ModelAndView model = new ModelAndView("questions/questions");
        model.addObject("specialView", new SpecialView(tagsReadService.getName(id),
                "/services/questions/tag/"+id));
        model.addObject(QUESTIONS_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public ModelAndView getQuestionsByUser(@PathVariable("id") long id,
                                     @RequestParam(name = "size", defaultValue = "10") String size,
                                     @RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "page", required = false) String page) throws InternalServerException {

        PageableContainer container =
                questionReadService.getQuestionsByUser(page, size,id, query, type, sort);

        ModelAndView model = new ModelAndView("questions/questions");
        model.addObject("specialView", new SpecialView(userReadService.getUsername(id),
                "/services/questions/user/"+id));
        model.addObject(QUESTIONS_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/user/answers/{id}",method = RequestMethod.GET)
    public ModelAndView getAnswersByUser(@PathVariable("id") long id,
                                     @RequestParam(name = "size", defaultValue = "10") String size,
                                     @RequestParam(name = "q", required = false) String query,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "sort", required = false) String sort) throws InternalServerException {

        ModelAndView model = new ModelAndView("questions/questions");
        model.addObject("specialView", new SpecialView(userReadService.getUsername(id),
                "/services/questions/user/answers/"+id));
        model.addObject(QUESTIONS_LIST_NAME,
                questionReadService.getAnswersByUser(0, size,id, query, type, sort));

        return model;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getQuestion(@PathVariable("id") long id,
                                    @SessionAttribute(name = "user", required = false) UserDTO userDTO) throws BadRequestException, NotFoundException{

        FullQuestionTransfer questionTransfer = questionReadService.getOneQuestion(id, userDTO);
        if(questionTransfer == null){
            throw  new NotFoundException();
        }

        return new ModelAndView("questions/question", QUESTION_DTO_NAME, questionTransfer);
    }

    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public ModelAndView askQuestion(){

        return new ModelAndView("questions/ask");
    }

    @RequestMapping(value = "/{id}/edit")
    public ModelAndView editQuestion(@PathVariable("id") long id,
                                     @SessionAttribute(name = "user", required = false) UserDTO userDTO) throws AccessDeniedException, NotFoundException, NonAuthorizedException {

        if(userDTO == null || userDTO.getId() == -1){
            throw new NonAuthorizedException();
        }

        FullQuestionTransfer fullQuestionTransfer = questionReadService.getOneQuestion(id, userDTO);
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
                                       @SessionAttribute(name = "user", required = false) UserDTO user) throws BadRequestException {

        if(bindingResult.hasErrors()){
            return new ModelAndView("questions/write");
        }
        if(user == null || user.getId() == -1){
            throw new BadRequestException();
        }

//        questionReadService.createQuestion(createQuestionDTO.getTitle(), tagsIDs, createQuestionDTO.getHtmlContent(), user.getId());

        return new ModelAndView("redirect:/questions");
    }
}
