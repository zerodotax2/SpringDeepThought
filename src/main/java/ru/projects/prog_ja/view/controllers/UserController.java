package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.interfaces.FactsService;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.view.exceptions.BadRequestException;
import ru.projects.prog_ja.view.exceptions.NotFoundException;

import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static final String FULL_USER_NAME="user";
    public static final String USER_LIST_NAME="userList";

    private final UserService userService;
    private final FactsService factsService;

    public UserController(@Autowired UserService userService,
                          @Autowired FactsService factsService){
        this.userService = userService;
        this.factsService = factsService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView user(@PathVariable("id") String strID) throws BadRequestException, NotFoundException {

        if(strID.matches("^\\d+$") || strID.length() > 64){
            throw new BadRequestException();
        }

        FullUserTransfer fullUserTransfer = userService.getFullUser(Long.parseLong(strID));
        if(fullUserTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("users/user", FULL_USER_NAME, fullUserTransfer);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(@RequestParam(name = "q", required = false) String query,
                              @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                              @RequestParam(name = "sort", required = false, defaultValue = "desc") int sort){

        ModelAndView model = new ModelAndView();

        List<SmallUserTransfer> users;
        if(!StringUtils.isEmpty(query)){

           users = userService.findSmallUsers(0,query, type, sort);
        }else{

            users = userService.getSmallUsers(0, type, sort);
        }

        model.addObject(USER_LIST_NAME, users);
        model.addObject(MainController.FACT_NAME, factsService.getRandomFact());
        model.setViewName("users/users");

        return model;
    }
}

