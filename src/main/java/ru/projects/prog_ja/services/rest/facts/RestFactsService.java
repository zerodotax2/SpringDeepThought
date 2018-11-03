package ru.projects.prog_ja.services.rest.facts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.view.create.CreateFactDTO;
import ru.projects.prog_ja.dto.view.update.UpdateFactDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.FactsWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<?> getFacts(@RequestParam(value = "q", required = false) String q,
                                          @RequestParam(value = "sort", defaultValue = "1") String sort,
                                          @RequestParam(value = "start") String start,
                                          @RequestParam(value = "type", defaultValue = "rating") String type){

        if(start == null || start.equals(""))
            return badRequest();
        else if(!start.matches("^\\d+&") || start.length() > 32)
            return incorrectFormat();

        List<CommonFactTransfer> facts = factsReadService.getFacts(Integer.parseInt(start),
                q, type, sort);
        if(facts == null || facts.size() == 0){
            return notFound();
        }

        return found(facts);

    }
    
    @GetMapping("/random")
    public ResponseEntity<?> getRandomFact(@RequestParam(value = "rate", defaultValue = "0") String rate,
                                           @RequestParam(value = "fact", required = false) String factId,
                                           @SessionAttribute("user") UserDTO userDTO){

        CommonFactTransfer factTransfer = factsReadService.getNextFact(factId, rate, userDTO);
        if(factTransfer == null){
            return serverError();
        }
        
        return found(factTransfer);
    }

    @PostMapping
    public ResponseEntity<?> addFact(@Valid @RequestBody CreateFactDTO createFactDTO, BindingResult bindingResult,
                                     @SessionAttribute("user")UserDTO userDTO){
        
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
                                        @SessionAttribute("user")UserDTO userDTO){
        
        if(userDTO == null || userDTO.getId() == -1)
            return accessDenied();

        if(factsWriteService.updateFact( updateFactDTO.getFactId(),
                updateFactDTO.getText(), updateFactDTO.getTags(), userDTO.getId())){
            return ok();
        }

        return serverError();

    }
   
}
