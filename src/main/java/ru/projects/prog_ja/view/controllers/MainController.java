package ru.projects.prog_ja.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    public static final String FACT_NAME = "fact";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(){

        return new ModelAndView("index");
    }
}
