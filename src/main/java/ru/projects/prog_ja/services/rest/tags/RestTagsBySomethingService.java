package ru.projects.prog_ja.services.rest.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.services.AbstractRestService;

@RestController
@RequestMapping(value = "/services/tags")
public class RestTagsBySomethingService extends AbstractRestService {

    private final TagsReadService tagsReadService;

    @Autowired
    public RestTagsBySomethingService(TagsReadService tagsReadService) {
        this.tagsReadService = tagsReadService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getTagsByUser(@PathVariable("id") long userId,
                                           @RequestParam(name = "size", defaultValue = "6") String size,
                                           @RequestParam(name = "q", required = false) String q,
                                           @RequestParam(name = "type", defaultValue = "rating") String type,
                                           @RequestParam(name = "sort", defaultValue = "1")String sort,
                                           @RequestParam(name = "page", defaultValue = "1") String page){


        return found(tagsReadService.getTagsByUser(page, size, userId, q, type, sort));
    }
}
