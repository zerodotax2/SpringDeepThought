package ru.projects.prog_ja.view.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.interfaces.TokenService;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.logic.util.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Слушатель успешно авторизации пользователя
* */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;


    public AuthSuccessHandler(@Autowired UserService userService){
        this.userService = userService;
    }

    /**
     * При успешно авторизации пользователя, находим его в бд и ложим в сессию его данные,
     * чтобы отображать их на странице
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        String login = authentication.getName();

        SmallUserTransfer userTransfer = userService.getUserByLogin(login);
        if(userTransfer == null){
            authentication.setAuthenticated(false);
            httpServletResponse.sendRedirect("/login");
            return;
        }

        /**
         * Также при авторизации добавляем к пользователю секретный токен
         * */
        if(!userService.createUserToken(userTransfer.getId(), httpServletResponse)){
            authentication.setAuthenticated(false);
            httpServletResponse.sendRedirect("/login");
            return;
        }

        httpServletRequest.getSession().setAttribute("user", userTransfer);
        httpServletResponse.sendRedirect("/articles");
    }

}
