package ru.projects.prog_ja.services.rest.facts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.List;

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
                                           @RequestParam(value = "size", defaultValue = "6") String size){


        BySomethingContainer container = factsReadService.getFactsByTag(0, size,id, "date", "1");
        if(container == null){
            return serverError();
        }

        return found(container);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getFactsByUser(@PathVariable("id") long id,
                                            @RequestParam(value = "size", defaultValue = "6") String size){


        BySomethingContainer container = factsReadService.getFactsByUser(0, size,id, "date", "1");
        if(container == null){
            return serverError();
        }

        return found(container);
    }

}
