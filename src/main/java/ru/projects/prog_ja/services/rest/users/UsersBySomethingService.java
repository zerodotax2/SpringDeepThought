package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.services.AbstractRestService;

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
                                           @RequestParam(name = "size", defaultValue = "6") String size,
                                           @RequestParam(name = "q", required = false) String q,
                                           @RequestParam(name = "type", defaultValue = "rating") String type,
                                           @RequestParam(name = "sort", defaultValue = "1")String sort,
                                           @RequestParam(name = "page", defaultValue = "1") String page) {


        return found(userReadService.getUsersByInterests(page, size, id, q, type, sort));
    }
}
