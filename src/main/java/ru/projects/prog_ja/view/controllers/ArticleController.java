package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.exceptions.NotFoundException;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {


    public final static String FULL_ARTICLE_DTO_NAME = "fullArticleDTO";
    public final static String CREATE_ARTICLE_DTO_NAME = "createArticleDTO";
    public final static String ARTICLE_LIST_NAME = "articles";
    public final static String ARTICLE_EDIT_NAME = "articleEdit";

    private final ArticleReadService articleReadService;

    @Autowired
    public ArticleController(ArticleReadService articleReadService){
        this.articleReadService = articleReadService;
    }

    /*
    * Если get метод, то просто отображаем посты на странице
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getArticles(@RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                    @RequestParam(name = "sort", required = false, defaultValue = "1") String sort) throws NotFoundException, BadRequestException {


        List<CommonArticleTransfer> commonArticleTransfers = articleReadService.getArticles(0, query, type, sort);
        if(commonArticleTransfers == null){
            throw new NotFoundException();
        }

        return new ModelAndView("articles/articles",
                ARTICLE_LIST_NAME, commonArticleTransfers);
    }


    @RequestMapping(value = "/{id}")
    public ModelAndView getArticle(@PathVariable("id") long id) throws NotFoundException {

        FullArticleTransfer fullArticleTransfer = articleReadService.getArticleByID(id);
        if(fullArticleTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("articles/article", FULL_ARTICLE_DTO_NAME, fullArticleTransfer);
    }



    @RequestMapping(value ="/write", method = RequestMethod.GET)
    public ModelAndView writeArticle(){

        return new ModelAndView("articles/articleWrite");
    }

    @RequestMapping(value = "/{id}/edit")
    public ModelAndView editArticle(@PathVariable("id") long id,
                                    @SessionAttribute UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
        if(userDTO == null)
            throw new NonAuthorizedException();

        FullArticleTransfer fullArticleTransfer = articleReadService.getArticleByID(id);
        if(fullArticleTransfer == null)
            throw new NotFoundException();

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == fullArticleTransfer.getUser().getId()){
            return new ModelAndView("articles/articleEdit", ARTICLE_EDIT_NAME, fullArticleTransfer);
        }else{

            throw new AccessDeniedException();
        }
    }


//    @Deprecated
//    @RequestMapping(value="/write", method = RequestMethod.POST)
//    public String createArticle(@Valid @ModelAttribute(CREATE_ARTICLE_DTO_NAME) CreateArticleDTO createArticleDTO, BindingResult bindingResult,
//                             @SessionAttribute("user") SmallUserTransfer user){
//        if(bindingResult.hasErrors()){
//            return "articles/write";
//        }
//
//        /*
//         * Проверяем, что id юзера в DTO совпадает с тем юзером,
//         * который его создаёт
//         * */
//        if(user.getId() != createArticleDTO.getUserId()){
//            return "articles/write";
//        }
//
//        /*
//         * Получаем строку тегов из DTO, проходимся по каждому из них,
//         * если один не совпадает с паттерном возвращаем ошибку
//         * */
//        List<Long> tagsIDs = new ArrayList<>();
//        String[] tags = createArticleDTO.getTagsSTR().split(" ");
//        if(tags.length > 5) return "articles/write";
//        for(String tag : tags){
//            if(!tag.matches("^\\d+$") || tag.length() > 64){
//                return "articles/write";
//            }
//            tagsIDs.add(Long.parseLong(tag));
//        }
//        /*
//         * Создаём JSON для разных размеров главного изображения
//         * */
//        ThreeImagesPathDTO threeImagesPathDTO = new ThreeImagesPathDTO(createArticleDTO.getSmallImagePath(),
//                createArticleDTO.getMiddleImagePath(), createArticleDTO.getLargeImagePath());
//
//        articleReadService.createArticle(threeImagesPathDTO, createArticleDTO.getTitle(), createArticleDTO.getSubtitle(),
//                createArticleDTO.getHtmlContent(), tagsIDs, createArticleDTO.getUserId());
//
//        return "redirect:/articles";
//    }


}
