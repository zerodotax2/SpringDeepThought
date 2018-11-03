package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.List;

@RestController
@RequestMapping("/services/users")
public class UsersBySomethingService extends AbstractRestService {

    private final UserReadService userReadService;

    @Autowired
    public  UsersBySomethingService(UserReadService userReadService){
        this.userReadService = userReadService;
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getUsersByTag(@PathVariable("id") long id,
                                           @RequestParam(value = "size", defaultValue = "6") String size) {


        BySomethingContainer container = userReadService.getUsersByInterests(0, size, id, "rating", "1");
        if (container == null) {
            return serverError();
        }

        return found(container);
    }
}
