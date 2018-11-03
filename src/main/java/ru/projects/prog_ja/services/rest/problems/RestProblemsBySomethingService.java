package ru.projects.prog_ja.services.rest.problems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.smalls.SmallProblemTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ProblemReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.List;

@RestController
@RequestMapping("/services/questions")
public class RestProblemsBySomethingService extends AbstractRestService {

    private final ProblemReadService problemReadService;

    @Autowired
    public RestProblemsBySomethingService(ProblemReadService problemReadService){
        this.problemReadService = problemReadService;
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getProblemsByTag(@PathVariable("id") long id,
                                              @RequestParam(value = "size", defaultValue = "6") String size){

        BySomethingContainer container = problemReadService.getProblemsByTag(0, size,id, "rating", "1");
        if(container == null){
            return notFound();
        }

        return found(container);
    }

    @GetMapping("/user/solved/{id}")
    public ResponseEntity<?> getSolvedProblemsByUser(@PathVariable("id") long id,
                                                     @RequestParam(value = "size", defaultValue = "6") String size){

        BySomethingContainer container = problemReadService
                .getProblemsSolvedByUser(0, size, id, "rating", "1");
        if(container == null){
            return notFound();
        }

        return found(container);
    }

    @GetMapping("/user/created/{id}")
    public ResponseEntity<?> getCreateProblemsByUser(@PathVariable("id") long id,
                                                     @RequestParam(value = "size", defaultValue = "6") String size){

        BySomethingContainer container = problemReadService
                .getProblemsByUser(0, size, id, "rating", "1");
        if(container == null){
            return notFound();
        }

        return found(container);
    }

}
