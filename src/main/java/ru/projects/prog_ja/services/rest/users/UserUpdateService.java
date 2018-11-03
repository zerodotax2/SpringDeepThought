package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.view.PasswordDTO;
import ru.projects.prog_ja.dto.view.TagsContainerDTO;
import ru.projects.prog_ja.dto.view.UpdateDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping("/services/user/update")
public class UserUpdateService extends AbstractRestService {

    private UserWriteService userWriteService;
    private AuthService authService;

    @Autowired
    public UserUpdateService(UserWriteService userWriteService, AuthService authService) {
        this.userWriteService = userWriteService;
        this.authService = authService;
    }

    @PostMapping("/bday")
    public ResponseEntity<?>  changeBirthdate(@RequestBody UpdateDTO updateDTO,
                                              @SessionAttribute("user")UserDTO userDTO){

        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null){
           return badRequest();
        }

        else if(!updateDTO.getValue().matches("^[\\d]{2}-[\\d]{2}-[\\d]{4}$"))
          return incorrectFormat();

        else if(userWriteService.updateBirthDate(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();

    }

    @PostMapping("/lname")
    public ResponseEntity<?> updateLastName(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(!updateDTO.getValue().matches("^[А-я]+$") ||
                updateDTO.getValue().length() < 3 || updateDTO.getValue().length() > 32)
            return incorrectFormat();

        else if(userWriteService.updateLastName(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/fname")
    public ResponseEntity<?> updateFirstName(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(!updateDTO.getValue().matches("^[А-я]+$") ||
                updateDTO.getValue().length() < 3 || updateDTO.getValue().length() > 32)
            return incorrectFormat();

        else if(userWriteService.updateFirstName(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/about")
    public ResponseEntity<?> updateAbout(@RequestBody UpdateDTO updateDTO,
                                            @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(updateDTO.getValue().length() < 10 || updateDTO.getValue().length() > 1000
                || !updateDTO.getValue().matches("^[\\w|\\s]+$"))
            return incorrectFormat();

        else if(userWriteService.updateAbout(userDTO.getId(), updateDTO.getValue())){
            return ok();
        }

        return serverError();
    }

    @PostMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateDTO updateDTO,
                                        @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(updateDTO == null || updateDTO.getValue() == null)
            return badRequest();

        else if(updateDTO.getValue().length() < 5 || updateDTO.getValue().length() > 50
                || !updateDTO.getValue().matches("^[\\w]+?@[\\w]+?\\.[\\w]+?$"))
            return incorrectFormat();

        else if(authService.updateEmail(userDTO.getId(), updateDTO.getValue()))
            return ok();

        return serverError();
    }
    @PostMapping("/interests")
    public ResponseEntity<?> updateInterests(@RequestBody TagsContainerDTO tagsContainerDTO,
                                         @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(tagsContainerDTO == null || tagsContainerDTO.getTags() == null)
            return badRequest();

        else if(tagsContainerDTO.getTags().size() < 3 || tagsContainerDTO.getTags().size() > 5)
            return incorrectFormat();

        else if(userWriteService.updateInterests(userDTO.getId(), tagsContainerDTO.getTags()))
            return ok();

        return serverError();
    }

    @PostMapping("/password")
    public ResponseEntity<?> updateInterests(@RequestBody PasswordDTO passwordDTO,
                                             @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() != -1)
            return accessDenied();

        else if(passwordDTO == null || passwordDTO.getOldPassword() == null || passwordDTO.getNewPassword() == null
                || !passwordDTO.getOldPassword().equals(passwordDTO.getNewPassword()))
            return badRequest();

        else if(authService.updatePass(passwordDTO.getNewPassword(), userDTO.getId()))
            return ok();

        return serverError();
    }

}
