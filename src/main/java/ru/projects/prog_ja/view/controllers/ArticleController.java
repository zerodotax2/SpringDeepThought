package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.SpecialView;
import ru.projects.prog_ja.exceptions.AccessDeniedException;
import ru.projects.prog_ja.exceptions.NonAuthorizedException;
import ru.projects.prog_ja.exceptions.NotFoundException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.TagsReadService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;

import java.util.Optional;

@Controller
@RequestMapping("/articles")
public class ArticleController {


    public static final String FULL_ARTICLE_DTO_NAME = "fullArticleDTO";
    public static final String CREATE_ARTICLE_DTO_NAME = "createArticleDTO";
    public static final String ARTICLE_LIST_NAME = "articles";
    public static final String ARTICLE_EDIT_NAME = "articleEdit";
    public static final String PAGES_NAME = "pages";

    private final ArticleReadService articleReadService;
    private final TagsReadService tagsReadService;
    private final UserReadService userReadService;

    @Autowired
    public ArticleController(ArticleReadService articleReadService,
                             UserReadService userReadService,
                             TagsReadService tagsReadService){
        this.articleReadService = articleReadService;
        this.tagsReadService = tagsReadService;
        this.userReadService = userReadService;
    }

    /*
    * Если get метод, то просто отображаем посты на странице
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getArticles(@RequestParam(name = "type") Optional<String> type,
                                    @RequestParam(name = "sort") Optional<String> sort,
                                    @RequestParam(name = "page") Optional<String> page,
                                    @RequestParam(name = "q") Optional<String> query) throws NotFoundException {




        PageableContainer container = articleReadService.getArticles(page.orElse(null),
                query.orElse(null), type.orElse(null), sort.orElse(null));

        ModelAndView model = new ModelAndView("articles/articles");
        model.addObject(ARTICLE_LIST_NAME, container.getList());
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/tag/{id}",method = RequestMethod.GET)
    public ModelAndView getArticlesByTag(@PathVariable("id") long id,
                                    @RequestParam(name = "size", defaultValue = "10") String size,
                                    @RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false) String type,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "page", required = false) String page) {

        ModelAndView model = new ModelAndView("articles/articles");
        PageableContainer container =
                this.articleReadService.getCommonArticlesByTag(page,size, id,query, type, sort);
        model.addObject(ARTICLE_LIST_NAME, container.getList());
        model.addObject("specialView", new SpecialView(this.tagsReadService.getName(id),
                "/services/articles/tag/" + id));
        model.addObject(PAGES_NAME, container.getPages());

        return model;
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public ModelAndView getArticlesByUser(@PathVariable("id") long id,
                                    @RequestParam(name = "size", defaultValue = "10") String size,
                                    @RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "page", required = false) String page,
                                    @RequestParam(name = "type", required = false) String type,
                                    @RequestParam(name = "sort", required = false) String sort) {


        ModelAndView model = new ModelAndView("articles/articles");
        PageableContainer container =
                this.articleReadService.getCommonArticlesByUser(page,size, id,query, type, sort);
        model.addObject(ArticleController.ARTICLE_LIST_NAME, container.getList());
        model.addObject("specialView", new SpecialView(this.userReadService.getUsername(id),
                "/services/articles/user/" + id));
        model.addObject(PAGES_NAME, container.getPages());

        return model;

    }


    @RequestMapping("/{id}")
    public ModelAndView getArticle(@PathVariable("id") long id,
                                   @SessionAttribute(name = "user", required = false) UserDTO userDTO) throws NotFoundException {

        FullArticleTransfer fullArticleTransfer = this.articleReadService.getArticleByID(id, userDTO);
        if(fullArticleTransfer == null){
            throw new NotFoundException();
        }

        return new ModelAndView("articles/article", ArticleController.FULL_ARTICLE_DTO_NAME, fullArticleTransfer);
    }



    @RequestMapping(value ="/write", method = RequestMethod.GET)
    public ModelAndView writeArticle(){

        return new ModelAndView("articles/articleWrite");
    }

    @RequestMapping("/{id}/edit")
    public ModelAndView editArticle(@PathVariable("id") long id,
                                    @SessionAttribute("user") UserDTO userDTO) throws NonAuthorizedException, NotFoundException, AccessDeniedException {
        if(userDTO == null)
            throw new NonAuthorizedException();

        FullArticleTransfer fullArticleTransfer = this.articleReadService.getArticleByID(id, userDTO);
        if(fullArticleTransfer == null)
            throw new NotFoundException();

        if(userDTO.getRole() == Role.ROLE_ADMIN || userDTO.getRole() == Role.ROLE_MODER
                || userDTO.getId() == fullArticleTransfer.getUser().getId()){
            return new ModelAndView("articles/articleEdit", ArticleController.ARTICLE_EDIT_NAME, fullArticleTransfer);
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
