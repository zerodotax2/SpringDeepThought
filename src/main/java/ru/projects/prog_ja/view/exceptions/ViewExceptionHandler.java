package ru.projects.prog_ja.view.exceptions;

import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NotFoundException;

@ControllerAdvice
public class ViewExceptionHandler {

    /**
     * handle not found resource exceptions
     * @return page to resolve this problem
     * */
    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView notFoundException(){

        return new ModelAndView("errors/404");
    }

    /**
     * handle bad user request exceptions
     * @return page to resolve this problem
     * */
    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView badRequestException(){

        return new ModelAndView("errors/400");
    }

    /**
     * handle server esceptions
     * @return page to resolve this problem
     * */
    @ExceptionHandler(value = InternalServerException.class)
    public ModelAndView internalServerException(){

        return new ModelAndView("errors/500");
    }

    @ExceptionHandler(value = HibernateException.class)
    public ModelAndView dataException(){

        return new ModelAndView("errors/500");
    }
}
