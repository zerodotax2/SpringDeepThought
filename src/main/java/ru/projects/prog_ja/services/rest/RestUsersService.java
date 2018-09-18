package ru.projects.prog_ja.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.projects.prog_ja.dto.view.RenderDTO;
import ru.projects.prog_ja.dto.view.SearchDTO;
import ru.projects.prog_ja.logic.services.interfaces.UserService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/user")
public class RestUsersService extends AbstractRestService {

    private final UserService userService;

    public RestUsersService(@Autowired UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@Valid @RequestBody RenderDTO renderDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List users;
        switch (renderDTO.getObjectSize()){
            case "small":
                users = userService.getSmallUsers(renderDTO.getStart(), renderDTO.getType(), renderDTO.getSort());
                break;
            case "common":
                users = userService.getCommonUsers(renderDTO.getStart(), renderDTO.getType(), renderDTO.getSort());
                break;

            default: return badRequest();
        }

        if(users == null){
            return serverError();
        }

        return found(users);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUsers(@Valid @RequestBody SearchDTO searchDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return badRequest();
        }

        List users;

        switch (searchDTO.getObjectSize()){
            case "small":
                users = userService.findSmallUsers(searchDTO.getStart(), searchDTO.getSearch(),
                        searchDTO.getType(),
                        searchDTO.getSort());
                break;
            case "common":
                users = userService.findCommonUsers(searchDTO.getStart(), searchDTO.getSearch(), searchDTO.getType(), searchDTO.getSort());

            default: return badRequest();
        }

        if(users == null){
            return serverError();
        }

        return found(users);

    }
}
