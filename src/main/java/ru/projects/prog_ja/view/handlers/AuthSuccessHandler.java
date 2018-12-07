package ru.projects.prog_ja.view.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Слушатель успешно авторизации пользователя
* */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Autowired
    public AuthSuccessHandler(AuthService authService){
        this.authService = authService;
    }

    /**
     * При успешно авторизации пользователя, находим его в бд и ложим в сессию его данные,
     * чтобы отображать их на странице
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        String login = authentication.getName();

        UserDTO userTransfer = authService.getUserByLogin(login);
        if(userTransfer == null){
            authentication.setAuthenticated(false);
            httpServletResponse.sendRedirect("/login?error=true");
            return;
        }

        httpServletRequest.getSession().setAttribute("user", userTransfer);
        if(userTransfer.getRole() == Role.ROLE_NONACTIVE) {
            httpServletResponse.sendRedirect("/account/nonactive");
            return;
        }

        String path = httpServletRequest.getRequestURL().toString();
        if(path.contains("auth"))
            httpServletResponse.sendRedirect("/articles");
        else
            httpServletResponse.sendRedirect(path);
    }


}
