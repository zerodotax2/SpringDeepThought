package ru.projects.prog_ja.services.rest.facts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping("/services/facts")
public class RestFactsBySomethingService extends AbstractRestService {

    private final FactsReadService factsReadService;

    @Autowired
    public RestFactsBySomethingService(FactsReadService factsReadService){
        this.factsReadService = factsReadService;
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getFactsByTag(@PathVariable("id") long id,
                                           @RequestParam(name = "size", defaultValue = "6") String size,
                                           @RequestParam(name = "q", required = false) String q,
                                           @RequestParam(name = "type", defaultValue = "rating") String type,
                                           @RequestParam(name = "sort", defaultValue = "1") String sort,
                                           @RequestParam(name = "page", defaultValue = "1") String page){


        return found(factsReadService.getFactsByTag(page, size,id,q, type, sort));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getFactsByUser(@PathVariable("id") long id,
                                            @RequestParam(name = "size", defaultValue = "6") String size,
                                            @RequestParam(name = "q", required = false) String q,
                                            @RequestParam(name = "type", defaultValue = "rating") String type,
                                            @RequestParam(name = "sort", defaultValue = "1")String sort,
                                            @RequestParam(name = "page", defaultValue = "1") String page){


        return found(factsReadService.getFactsByUser(page, size,id, q, type, sort));
    }

}
