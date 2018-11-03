package ru.projects.prog_ja.services.rest.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.view.FeedbackDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.SupportService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/support")
public class RestSupportService extends AbstractRestService {

    private final SupportService supportService;

    @Autowired
    public RestSupportService(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult bindingResult,
                                 @SessionAttribute("user")UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(supportService.ask(feedbackDTO.getText(), userDTO.getId())){
            return ok();
        }

        return serverError();
    }
}
