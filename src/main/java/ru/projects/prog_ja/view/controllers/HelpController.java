package ru.projects.prog_ja.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/support")
public class HelpController {

    public HelpController() {
    }

    @GetMapping
    public ModelAndView support(){

        return new ModelAndView("info/support");
    }

}
