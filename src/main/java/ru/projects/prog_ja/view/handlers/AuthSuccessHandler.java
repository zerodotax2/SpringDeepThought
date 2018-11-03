package ru.projects.prog_ja.view.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Слушатель успешно авторизации пользователя
* */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private UserReadService userReadService;


    public AuthSuccessHandler(@Autowired UserReadService userReadService){
        this.userReadService = userReadService;
    }

    /**
     * При успешно авторизации пользователя, находим его в бд и ложим в сессию его данные,
     * чтобы отображать их на странице
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        String login = authentication.getName();

        UserDTO userTransfer = userReadService.getUserByLogin(login);
        if(userTransfer == null){
            authentication.setAuthenticated(false);
            httpServletResponse.sendRedirect("/login?error=true");
            return;
        }

        /**
         * Также при авторизации добавляем к пользователю секретный токен
         * */
        if(!userReadService.createUserToken(userTransfer.getId(), httpServletResponse)){
            authentication.setAuthenticated(false);
            httpServletResponse.sendRedirect("/login?error=true");
            return;
        }

        httpServletRequest.getSession().setAttribute("user", userTransfer);
        httpServletResponse.sendRedirect("/articles");
    }

}
