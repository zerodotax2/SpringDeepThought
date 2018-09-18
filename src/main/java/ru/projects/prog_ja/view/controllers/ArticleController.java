package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.dto.view.create.CreateArticleDTO;
import ru.projects.prog_ja.logic.services.interfaces.ArticleService;
import ru.projects.prog_ja.logic.services.interfaces.FactsService;
import ru.projects.prog_ja.view.exceptions.BadRequestException;
import ru.projects.prog_ja.view.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {


    public final static String FULL_ARTICLE_DTO_NAME = "fullArticleDTO";
    public final static String CREATE_ARTICLE_DTO_NAME = "createArticleDTO";
    public final static String ARTICLE_LIST_NAME = "articleList";

    private final ArticleService articleService;
    private final FactsService factsService;

    public ArticleController(@Autowired ArticleService articleService,
                             @Autowired FactsService factsService){
        this.articleService = articleService;
        this.factsService = factsService;
    }

    /*
    * Если get метод, то просто отображаем посты на странице
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getArticles(@RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "type", required = false, defaultValue = "rating") String type,
                                    @RequestParam(name = "sort", required = false, defaultValue = "desc") int sort) throws NotFoundException {

        ModelAndView model = new ModelAndView();

        List<CommonArticleTransfer> commonArticleTransfers;

        if(!StringUtils.isEmpty(query)){

            commonArticleTransfers = articleService.findArticles(0, query, type, sort);
        }else{

            commonArticleTransfers = articleService.getMiddleArticles(0, type, sort);
        }
        if(commonArticleTransfers == null){
            throw new NotFoundException();
        }
        /*
        * Получаем последние посты из базы данных
        * Возвращаем найденные посты
        * */
        model.addObject(ARTICLE_LIST_NAME, commonArticleTransfers);
        model.addObject(MainController.FACT_NAME, factsService.getRandomFact());
        model.setViewName("articles/articles");

        return model;
    }


    @RequestMapping(value = "/{id}")
    public ModelAndView getArticle(@PathVariable("id") String strID) throws BadRequestException, NotFoundException {

        /*
        * Если id не подходит по типу, то возвращаем bad request, который перехватится слушателем
        * */
        if(!strID.matches("^\\d+$") || strID.length() > 64){
            throw new BadRequestException();
        }

        /*
        * Получаем long id по строке и возврашаем найденный пост
        * */
        long id = Long.parseLong(strID);

        FullArticleTransfer fullArticleTransfer = articleService.getArticleByID(id);
        if(fullArticleTransfer == null){
            throw  new NotFoundException();
        }

        return new ModelAndView("articles/article", FULL_ARTICLE_DTO_NAME, fullArticleTransfer);
    }



    @RequestMapping(value ="/write", method = RequestMethod.GET)
    public ModelAndView writeArticle(){

        return new ModelAndView("articles/write", CREATE_ARTICLE_DTO_NAME, new CreateArticleDTO());
    }


    @Deprecated
//    @RequestMapping(value="/write", method = RequestMethod.POST)
    public String createPost(@Valid @ModelAttribute(CREATE_ARTICLE_DTO_NAME) CreateArticleDTO createArticleDTO, BindingResult bindingResult,
                             @SessionAttribute("user") SmallUserTransfer user){
        if(bindingResult.hasErrors()){
            return "articles/write";
        }

        /*
         * Проверяем, что id юзера в DTO совпадает с тем юзером,
         * который его создаёт
         * */
        if(user.getId() != createArticleDTO.getUserId()){
            return "articles/write";
        }

        /*
         * Получаем строку тегов из DTO, проходимся по каждому из них,
         * если один не совпадает с паттерном возвращаем ошибку
         * */
        List<Long> tagsIDs = new ArrayList<>();
        String[] tags = createArticleDTO.getTagsSTR().split(" ");
        if(tags.length > 5) return "articles/write";
        for(String tag : tags){
            if(!tag.matches("^\\d+$") || tag.length() > 64){
                return "articles/write";
            }
            tagsIDs.add(Long.parseLong(tag));
        }
        /*
         * Создаём JSON для разных размеров главного изображения
         * */
        ThreeImagesPathDTO threeImagesPathDTO = new ThreeImagesPathDTO(createArticleDTO.getSmallImagePath(),
                createArticleDTO.getMiddleImagePath(), createArticleDTO.getLargeImagePath());

        articleService.createArticle(threeImagesPathDTO, createArticleDTO.getTitle(), createArticleDTO.getSubtitle(),
                createArticleDTO.getHtmlContent(), tagsIDs, createArticleDTO.getUserId());

        return "redirect:/articles";
    }


}
