package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.view.create.CreateUserDTO;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.simple.interfaces.CookieService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.logic.singletons.interfaces.EmailPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {

    public static final String ERROR_DTO_NAME = "errorDTO";
    public static final String LOGIN_DTO_NAME = "loginUserDTO";
    public static final String CREATE_USER_DTO_NAME = "createUserDTO";

    private final AuthService authService;
    private final CookieService cookieService;
    private final EmailPaths emailPaths;

    @Autowired
    public AuthController(AuthService authService,
                          CookieService cookieService,
                          EmailPaths emailPaths){
        this.authService = authService;
        this.cookieService = cookieService;
        this.emailPaths = emailPaths;
    }

    @RequestMapping(value= "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(name = "error", required = false) String error){

        /*
         * Проверяем, что пользователь уже не авторизован, тогда делаем redirect
         * */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getPrincipal().toString().equals("anonymousUser")){
            return new ModelAndView("redirect:/articles");
        }

        return new ModelAndView("auth/login");
    }


    //    @RequestMapping(value = "/login/auth", method = RequestMethod.POST)
//    @Deprecated
//    public ModelAndView auth(@Valid @ModelAttribute(LOGIN_DTO_NAME) LoginUserDTO loginUserDTO, BindingResult bindingResult,
//                             @ModelAttribute(ERROR_DTO_NAME) ErrorDTO errorDTO,
//                             HttpServletResponse response){
//        if(bindingResult.hasErrors()){
//            return new ModelAndView("auth/login", ERROR_DTO_NAME, "error");
//        }
//
//        /*
//         * Пытаемся авторизовать пользователя
//         * */
//        UserDTO user = authService.checkUser(loginUserDTO.getLogin(), loginUserDTO.getPassword(), loginUserDTO.isCreateCookie(), response);
//        if(user == null){
//            return new ModelAndView("auth/login",ERROR_DTO_NAME, "error");
//        }
//
//        /*
//         * Если пользователь найден, то добавляем его в сессию и возвращаем её
//         * */
//        return new ModelAndView("redirect:/articles");
//    }



    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus sessionStatus, HttpSession session, HttpServletRequest request, HttpServletResponse response){

        /*
         * Удаляем куки из сессии пользователя
         * */
        cookieService.removeCookies(request, response);

        /*Удаялем пользователя из Security контекста*/
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

        /*
         * Удаляем Spring сессию, а затем http сессию
         * */
        sessionStatus.setComplete();
        session.invalidate();

        return "redirect:/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@RequestParam(name = "error", required = false) String error){

        /*
         * Проверяем, что пользователь уже не авторизован, тогда делаем redirect
         * */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getPrincipal().toString().equals("anonymousUser")){
            return new ModelAndView("redirect:/articles");
        }

        return new ModelAndView("auth/register", CREATE_USER_DTO_NAME, new CreateUserDTO());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute(CREATE_USER_DTO_NAME) CreateUserDTO createUserDTO,
                                   BindingResult bindingResult,
                                   ModelAndView model, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ModelAndView("redirect:/register?error");
        }

        /*
         * Пытаемся создать пользователя, если получилось, то добавляем его в сессию и возвращаем view
         * */
        UserDTO user = authService.createUser(createUserDTO.getLogin(), createUserDTO.getPassword(), createUserDTO.getEmail(), createUserDTO.isCreateCookie(), response);

        if(user == null){
            return new ModelAndView("redirect:/register?error");
        }

        return new ModelAndView("auth/email", "mailhelper",
                emailPaths.getLinkByEmail(createUserDTO.getEmail()));
    }


    @RequestMapping(value = "/account/activate/{token}", method = RequestMethod.GET)
    public ModelAndView activate(@PathVariable("token") String token) throws NotFoundException {

        if(authService.activateAccount(token))
            return new ModelAndView("auth/activated");

        throw new NotFoundException();
    }

    @RequestMapping(value = "/account/nonactive", method = RequestMethod.GET)
    public ModelAndView nonactive(@SessionAttribute(name = "user") UserDTO user) throws NotFoundException {
        if(user == null || user.getId() == -1 || user.getRole() != Role.ROLE_NONACTIVE)
            throw new NotFoundException();

        return new ModelAndView("errors/nonactive");
    }

    @RequestMapping(value = "/restore", method = RequestMethod.GET)
    public ModelAndView restore(){

        return new ModelAndView("auth/restore");
    }

    @GetMapping("/restore/{token}")
    public ModelAndView restoreByToken(@PathVariable("token") String token) throws AccessDeniedException {

        if(!authService.containsRestoreToken(token))
            throw new AccessDeniedException();

        return new ModelAndView("auth/changePassword", "token", token);
    }

    @GetMapping(value = "/account/email/activate/{token}")
    public ModelAndView activateEmail(@PathVariable("token") String token) throws NotFoundException {

        if(authService.activateEmail(token))
            return new ModelAndView("auth/activateEmail");

        throw new NotFoundException();
    }
}
