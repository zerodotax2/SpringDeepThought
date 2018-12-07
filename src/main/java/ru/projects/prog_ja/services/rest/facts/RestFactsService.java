package ru.projects.prog_ja.services.rest.facts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.view.create.CreateFactDTO;
import ru.projects.prog_ja.dto.view.update.UpdateFactDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/facts")
public class RestFactsService extends AbstractRestService {
    
    private final FactsReadService factsReadService;
    private final FactsWriteService factsWriteService;

    @Autowired
    public RestFactsService(FactsWriteService factsWriteService,
                            FactsReadService factsReadService){
        this.factsReadService = factsReadService;
        this.factsWriteService = factsWriteService;
    }

    @GetMapping
    public ResponseEntity<?> getFacts(@RequestParam(name = "q", required = false) String q,
                                          @RequestParam(name = "sort", defaultValue = "1") String sort,
                                          @RequestParam(name = "page", defaultValue = "1") String page,
                                          @RequestParam(name = "type", defaultValue = "rating") String type){

        return found(factsReadService.getFacts(page, q, type, sort));
    }
    
    @GetMapping("/random")
    public ResponseEntity<?> getRandomFact(@RequestParam(name = "rate", defaultValue = "0") String rate,
                                           @RequestParam(name = "fact", required = false) String factId,
                                           @SessionAttribute(name = "user", required = false) UserDTO userDTO){

        CommonFactTransfer factTransfer = factsReadService.getNextFact(factId, rate, userDTO);
        if(factTransfer == null){
            return serverError();
        }
        
        return found(factTransfer);
    }

    @PostMapping
    public ResponseEntity<?> addFact(@Valid @RequestBody CreateFactDTO createFactDTO, BindingResult bindingResult,
                                     @SessionAttribute(name = "user", required = false)UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        CommonFactTransfer fullFactTransfer = factsWriteService.createFact(
                createFactDTO.getText(), createFactDTO.getTags(), userDTO.getId());
        if(fullFactTransfer == null)
            return serverError();
        
        return accepted(fullFactTransfer);
    }
    
    @PutMapping
    public ResponseEntity<?> updateFact(@Valid @RequestBody UpdateFactDTO updateFactDTO, BindingResult bindingResult,
                                        @SessionAttribute(name = "user", required = false)UserDTO userDTO){
        
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(factsWriteService.updateFact( updateFactDTO.getFactId(),
                updateFactDTO.getText(), updateFactDTO.getTags(), userDTO.getId())){
            return ok();
        }

        return serverError();

    }
   
}
