package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.RenderDTO;
import ru.projects.prog_ja.dto.view.SearchDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/services/user")
public class RestUsersService extends AbstractRestService {

    private final UserReadService userReadService;

    public RestUsersService(@Autowired UserReadService userReadService){
        this.userReadService = userReadService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "q", required = false) String q,
                                      @RequestParam(value = "sort", defaultValue = "1") String sort,
                                      @RequestParam(value = "start") String start,
                                      @RequestParam(value = "type", defaultValue = "rating") String type){

        if(start == null || start.equals(""))

            return badRequest();
        else if(!start.matches("^\\d+$") || start.length() > 32){

            return incorrectFormat();
        }else{
            List<SmallUserTransfer> users = userReadService.getUsers(Integer.parseInt(start), q, type, sort);
            if(users == null){
                return serverError();
            }

            return found(users);
        }
    }
}
