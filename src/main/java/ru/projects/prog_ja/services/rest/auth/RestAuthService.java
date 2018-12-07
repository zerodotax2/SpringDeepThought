package ru.projects.prog_ja.services.rest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.EmailContainer;
import ru.projects.prog_ja.dto.auth.ExistsDTO;
import ru.projects.prog_ja.dto.auth.RestorePasswordContainer;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.view.create.CreateUserDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.AuthService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/services")
public class RestAuthService extends AbstractRestService {

    private final AuthService authService;

    @Autowired
    public RestAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/restore")
    public ResponseEntity<?> sendToEmail(@Valid @RequestBody EmailContainer email, BindingResult result){

            if(result.hasErrors())
            return badRequest();

        if(authService.restore(email.getEmail()))
            return ok();

        return serverError();
    }

    @PostMapping("/restore/update")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody RestorePasswordContainer password,
                                            BindingResult bindingResult){
        if(bindingResult.hasErrors() || !password.getPassword().equals(password.getConfirmPassword()))
            return badRequest();

        if(authService.restorePassword(password.getToken(), password.getPassword()))
            return ok();

        return serverError();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDTO userDTO,
                                      BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors())
            return badRequest();

        UserDTO user = authService.createUser(userDTO.getLogin(), userDTO.getPassword(), userDTO.getEmail(), userDTO.isCreateCookie(),
                    response);
        if(user == null)
            return serverError();


        return ok();
    }

    @PostMapping("/register/exists")
    public ResponseEntity<?> exist(@Valid @RequestBody ExistsDTO existsDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return badRequest();

        if(existsDTO.getType().equals("email")) {
            if (authService.existEmail(existsDTO.getValue()))
                return ok();
        }else if(existsDTO.getType().equals("login")) {
            if (authService.existLogin(existsDTO.getValue()))
                return ok();
        }
        else
            return badRequest();

        return isFree();
    }

    @PostMapping("/register/email")
    public ResponseEntity<?> sendActivateEmail(@SessionAttribute("user") UserDTO user){

        if(user == null || user.getId() == -1 || user.getRole() != Role.ROLE_NONACTIVE)
            return badRequest();

        String email = authService.resendActivateEmail(user.getId());
        if(email != null)
            return found(new EmailContainer(email));

        return serverError();
    }
}
