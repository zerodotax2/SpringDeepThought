package ru.projects.prog_ja.services.rest.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.NoticeService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping("/services/users")
public class RestUsersService extends AbstractRestService {

    private final UserReadService userReadService;
    private final NoticeService noticeService;

    @Autowired
    public RestUsersService(UserReadService userReadService,
                            NoticeService noticeService){
        this.userReadService = userReadService;
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(name = "q", required = false) String q,
                                      @RequestParam(name = "sort", defaultValue = "1") String sort,
                                      @RequestParam(name = "page", defaultValue = "1") String page,
                                      @RequestParam(name = "type", defaultValue = "rating") String type){

        return found(userReadService.getUsers(page, q, type, sort));
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<?> getNotices(@PathVariable("id") long id,
                                        @SessionAttribute("user") UserDTO user){

        if(user == null || user.getId() == -1 || user.getId() != id){
            return badRequest();
        }
        user.setNotices(0);
        return found(noticeService.getLastNotices(id));
    }
}
