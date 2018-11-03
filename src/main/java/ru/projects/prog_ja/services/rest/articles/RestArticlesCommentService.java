package ru.projects.prog_ja.services.rest.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.view.RemoveDTO;
import ru.projects.prog_ja.dto.view.create.CreateCommentDTO;
import ru.projects.prog_ja.dto.view.update.UpdateRatingDTO;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleWriteService;
import ru.projects.prog_ja.services.AbstractRestService;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/articles/comments")
public class RestArticlesCommentService extends AbstractRestService {

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    @Autowired
    public RestArticlesCommentService(ArticleReadService articleReadService,
                                      ArticleWriteService articleWriteService){
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @PostMapping
    public ResponseEntity<?> postComment(@Valid @RequestBody CreateCommentDTO createCommentDTO, BindingResult bindingResult,
                                         @SessionAttribute UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        CommonCommentTransfer commonCommentTransfer = articleWriteService.addComment(createCommentDTO.getId(), createCommentDTO.getText(), userDTO.getId());
        if(commonCommentTransfer == null){
            return serverError();
        }
        return found(commonCommentTransfer);
    }

    @PutMapping
    public ResponseEntity<?> changeComment(@Valid @RequestBody CreateCommentDTO createCommentDTO, BindingResult bindingResult,
                                           @SessionAttribute("user") UserDTO userDTO){
        if(bindingResult.hasErrors())
            return badRequest();

        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        if(articleWriteService.updateComment(createCommentDTO.getId(), createCommentDTO.getText(), userDTO.getId())){
            return found(createCommentDTO);
        }
        return serverError();
    }

    @DeleteMapping
    public ResponseEntity<?> removeComment(@RequestBody RemoveDTO removeDTO,
                                            @SessionAttribute("user") UserDTO userDTO){
        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        if(articleWriteService.removeComment(removeDTO.getId(), userDTO.getId())){
            return found(removeDTO);
        }
        return serverError();
    }

    @PostMapping(value = "/rating")
    public ResponseEntity<?> changeRate(@Valid @RequestBody UpdateRatingDTO updateRatingDTO, BindingResult bindingResult,
                                        @SessionAttribute("user") UserDTO userDTO){

        if(bindingResult.hasErrors())
            return badRequest();
        if(userDTO == null || userDTO.getId() == -1){
            return accessDenied();
        }
        if(articleWriteService.updateCommentRate(updateRatingDTO.getId(), updateRatingDTO.getRate(), userDTO.getId())){
            return found(updateRatingDTO);
        }
        return serverError();
    }
}
