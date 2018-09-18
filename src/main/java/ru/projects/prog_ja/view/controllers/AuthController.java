package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.dto.view.create.CreateUserDTO;
import ru.projects.prog_ja.view.dto.ErrorDTO;
import ru.projects.prog_ja.view.dto.LoginUserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@SessionAttributes("user")
public class AuthController {

    public static final String ERROR_DTO_NAME = "errorDTO";
    public static final String LOGIN_DTO_NAME = "loginUserDTO";
    public static final String CREATE_USER_DTO_NAME = "createUserDTO";

    private final UserService userService;


    public AuthController(@Autowired UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value= "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error){

        /*
         * Проверяем, что пользователь уже не авторизован, тогда делаем redirect
         * */
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            return new ModelAndView("redirect:/articles");
        }

        ModelAndView model = new ModelAndView();

        model.setViewName("auth/login");

//        model.addObject(LOGIN_DTO_NAME, new LoginUserDTO());
        model.addObject(ERROR_DTO_NAME, error);

        return model;
    }


    //    @RequestMapping(value = "/login/auth", method = RequestMethod.POST)
    @Deprecated
    public ModelAndView auth(@Valid @ModelAttribute(LOGIN_DTO_NAME) LoginUserDTO loginUserDTO, BindingResult bindingResult,
                             @ModelAttribute(ERROR_DTO_NAME) ErrorDTO errorDTO,
                             HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ModelAndView("auth/login", ERROR_DTO_NAME, "error");
        }

        /*
         * Пытаемся авторизовать пользователя
         * */
        SmallUserTransfer user = userService.checkUser(loginUserDTO.getLogin(), loginUserDTO.getPassword(), loginUserDTO.isCreateCookie(), response);
        if(user == null){
            return new ModelAndView("auth/login",ERROR_DTO_NAME, "error");
        }

        /*
         * Если пользователь найден, то добавляем его в сессию и возвращаем её
         * */
        return new ModelAndView("redirect:/articles", "user", user);
    }



    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus sessionStatus, HttpSession session, HttpServletRequest request, HttpServletResponse response){

        /*
         * Удаляем куки из сессии пользователя
         * */
        userService.removeCookies(request, response);

        /*
         * Удаляем Spring сессию, а затем http сессию
         * */
        sessionStatus.setComplete();
        session.invalidate();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

        return "redirect:/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@RequestParam(value = "error", required = false) String error){

        /*
         * Проверяем, что пользователь уже не авторизован, тогда делаем redirect
         * */
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            return new ModelAndView("redirect:/articles");
        }

        ModelAndView model = new ModelAndView();

        model.setViewName("auth/register");

        model.addObject(CREATE_USER_DTO_NAME, new CreateUserDTO());

        return model;
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute(CREATE_USER_DTO_NAME) CreateUserDTO createUserDTO, BindingResult bindingResult,
                                   HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ModelAndView("auth/register");
        }

        /*
         * Пытаемся создать пользователя, если получилось, то добавляем его в сессию и возвращаем view
         * */
        SmallUserTransfer user = userService.createUser(createUserDTO.getLogin(), createUserDTO.getPassword(), createUserDTO.getEmail(), createUserDTO.isCreateCookie(), response);
        if(user == null){
            return new ModelAndView("redirect:/login?error");
        }

        return new ModelAndView("redirect:/login", "user", user);
    }


}
