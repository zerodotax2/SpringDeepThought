package ru.projects.prog_ja.services.rest.problems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping("/services/problems")
public class RestProblemsBySomethingService extends AbstractRestService {

    private final ProblemReadService problemReadService;

    @Autowired
    public RestProblemsBySomethingService(ProblemReadService problemReadService){
        this.problemReadService = problemReadService;
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getProblemsByTag(@PathVariable("id") long id,
                                              @RequestParam(name = "size", defaultValue = "6") String size,
                                              @RequestParam(name = "q", required = false) String q,
                                              @RequestParam(name = "type", defaultValue = "rating") String type,
                                              @RequestParam(name = "sort", defaultValue = "1")String sort,
                                              @RequestParam(name = "difficult", required = false) String difficult,
                                              @RequestParam(name = "page", defaultValue = "1") String page){


        return found(problemReadService.getProblemsByTag(page, size,id, difficult, q, type, sort));
    }

    @GetMapping("/user/solved/{id}")
    public ResponseEntity<?> getSolvedProblemsByUser(@PathVariable("id") long id,
                                                     @RequestParam(name = "size", defaultValue = "6") String size,
                                                     @RequestParam(name = "q", required = false) String q,
                                                     @RequestParam(name = "type", defaultValue = "rating") String type,
                                                     @RequestParam(name = "sort", defaultValue = "1")String sort,
                                                     @RequestParam(name = "difficult", required = false) String difficult,
                                                     @RequestParam(name = "page", defaultValue = "1") String page){

        return found(problemReadService
                .getProblemsSolvedByUser(page, size, id, difficult, q, type, sort));
    }

    @GetMapping("/user/created/{id}")
    public ResponseEntity<?> getCreateProblemsByUser(@PathVariable("id") long id,
                                                     @RequestParam(name = "size", defaultValue = "6") String size,
                                                     @RequestParam(name = "q", required = false) String q,
                                                     @RequestParam(name = "type", defaultValue = "rating") String type,
                                                     @RequestParam(name = "sort", defaultValue = "1")String sort,
                                                     @RequestParam(name = "difficult", required = false) String difficult,
                                                     @RequestParam(name = "page", defaultValue = "1") String page){

        return found(problemReadService
                .getProblemsByUser(page, size, id,difficult, q, type,sort));
    }

}
