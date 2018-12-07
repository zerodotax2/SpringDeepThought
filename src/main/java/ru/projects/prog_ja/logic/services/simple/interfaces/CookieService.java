package ru.projects.prog_ja.logic.services.simple.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface CookieService {

    boolean removeCookies(HttpServletRequest request, HttpServletResponse response);

    boolean addCookies(Map<String, String> map, HttpServletResponse response);

    Map<String, String> findCookies(HttpServletRequest request);

    boolean addLogPassCookies(String email_h, String pass_h, HttpServletResponse response);

}
