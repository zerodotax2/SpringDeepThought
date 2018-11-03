package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.InternalServerException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;

import java.util.List;

@Controller
@RequestMapping("/facts")
public class FactsController {

    private static final String FACT_LIST_NAME = "facts";
    private static final String FACT_EDIT_NAME = "factEdit";

    private FactsReadService factsReadService;

    @Autowired
    public FactsController(FactsReadService factsReadService) {
        this.factsReadService = factsReadService;
    }

    @GetMapping
    public ModelAndView getFacts(@RequestParam(value = "q", required = false) String query,
                                 @RequestParam(value = "sort", defaultValue = "1") String sort,
                                 @RequestParam(value = "type", defaultValue = "rating") String type) throws InternalServerException {

        List<CommonFactTransfer> commonFactTransfers = factsReadService.getFacts(0, query, type, sort);
        if(commonFactTransfers == null)
            throw new InternalServerException();

        return new ModelAndView("facts/facts", FACT_LIST_NAME, commonFactTransfers);
    }

    @GetMapping("/add")
    public ModelAndView addFact(){

        return new ModelAndView("facts/factAdd");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editFact(@PathVariable("id") long id,
                                 @SessionAttribute("user")UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
        if(userDTO == null || userDTO.getId() == -1)
            throw new NonAuthorizedException();

        FullFactTransfer factTransfer = factsReadService.getFullFact(id);
        if(factTransfer == null)
            throw new NotFoundException();

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == factTransfer.getUser().getId()){

            return new ModelAndView("facts/factEdit", FACT_EDIT_NAME, factTransfer);
        }else{

            throw new AccessDeniedException();
        }
    }
}
