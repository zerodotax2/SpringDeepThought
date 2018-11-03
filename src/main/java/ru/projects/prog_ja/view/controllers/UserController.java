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
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NotFoundException;

import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static final String FULL_USER_NAME="user";
    public static final String USER_LIST_NAME="users";

    private final UserReadService userReadService;

    @Autowired
    public UserController( UserReadService userReadService){
        this.userReadService = userReadService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView user(@PathVariable("id") Long id) throws NotFoundException {


        FullUserTransfer fullUserTransfer = userReadService.getFullUser(id);
        if(fullUserTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("users/user", FULL_USER_NAME, fullUserTransfer);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(@RequestParam(name = "q", required = false) String query,
                              @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                              @RequestParam(name = "sort", required = false, defaultValue = "1") String sort) throws InternalServerException {

        List<SmallUserTransfer> users = userReadService.getUsers(0, query, type, sort);
        if(users == null)
            throw new InternalServerException();


        return new ModelAndView("users/users", USER_LIST_NAME, users);
    }
}

