package ru.projects.prog_ja.logic.services.simple.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.logic.services.simple.interfaces.CookieService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Scope("prototype")
public class CookieServiceImpl implements CookieService {


    private final AuthService authService;

    @Autowired
    public CookieServiceImpl(AuthService authService){
        this.authService = authService;
    }

    @Override
    public boolean removeCookies(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);//Устанавливаем время жизни кукисов на 0 секунд (такой способ удаления)
            response.addCookie(cookie);
        }

        return false;
    }

    @Override
    public boolean addCookies(Map<String, String> map, HttpServletResponse response) {
        /*Получаем все ключи в мапе куков*/
        Set<String> keySet = map.keySet();

        /*Выполняем перебор по всем ключам*/
        for(String key : keySet){
            /*Получаем значение по ключу данного цикла*/
            String value = map.get(key);
            /*Добавляем куки в ответ*/
            Cookie cookie = new Cookie(key, value);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 365);

            response.addCookie(cookie);
        }

        return true;
    }

    @Override
    public Map<String, String> findCookies(HttpServletRequest request) {
        if(request == null)
            return Collections.emptyMap();

        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();

        if(cookies == null)
            return Collections.emptyMap();

        for(Cookie c : cookies){

            cookieMap.put(c.getName(), c.getValue());

        }

        return cookieMap;
    }

    /**
     * checking if mail_h and pass_h in cookies exist
     * if yes then check user with this mail_g and pass_h
     * and return founded
     * */
    @Override
    public UserDTO getUserByCookies(HttpServletRequest request) {

        Map<String, String> cookieMap = findCookies(request);

        if(!cookieMap.containsKey("login") || !cookieMap.containsKey("password_h"))
            return null;

        return authService.checkUser(cookieMap.get("login"),
                cookieMap.get("password_h"), false, null);
    }

    /**
     * adding email_h and pass_h in cookies
     * */
    @Override
    public boolean addLogPassCookies(String login_h, String pass_h, HttpServletResponse response) {
        /*
            Добавляем в куки пользователя email и пароль, для того, чтобы
            не приходилось каждый раз логиниться на сайт
        */
        if(response == null){
            return false;
        }

        addCookies(new HashMap<String, String>(){{
            put("login", login_h);
            put("password_h", pass_h);
        }}, response);

        return true;
    }
}
