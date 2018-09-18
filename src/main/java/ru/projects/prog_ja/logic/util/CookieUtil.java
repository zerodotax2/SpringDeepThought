package ru.projects.prog_ja.logic.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CookieUtil {


    /*
    * Метод добавляет в response карту переданных значений в виду куков
    */
    public void addCookies(Map<String, String> cookieMap, HttpServletResponse response){

        /*Получаем все ключи в мапе куков*/
        Set<String> keySet = cookieMap.keySet();

        /*Выполняем перебор по всем ключам*/
        for(String key : keySet){
            /*Получаем значение по ключу данного цикла*/
            String value = cookieMap.get(key);
            /*Добавляем куки в ответ*/
            Cookie cookie = new Cookie(key, value);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 365);

            response.addCookie(cookie);
        }

    }


    /*
    * Метод создаёт мапу, которая содержит все имена куки и их значения,
    * дальше заполняет ее значениями куки из request и возвраащает
    */
    public Map<String, String> findCookies(HttpServletRequest request){

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


    /*
    * Метод пуолчаем из request все куки, которые есть и удаляет их
    */
    public void removeCookies(HttpServletRequest request,HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_OK);

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);//Устанавливаем время жизни кукисов на 0 секунд (такой способ удаления)
            response.addCookie(cookie);
        }
    }
}
