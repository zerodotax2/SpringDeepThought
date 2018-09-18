package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.logic.services.interfaces.ArticleService;
import ru.projects.prog_ja.logic.util.XSSGuard;
import ru.projects.prog_ja.model.dao.ArticleDAO;

import java.util.*;
import java.util.concurrent.Future;

@Service
@Scope( scopeName = "prototype")
public class ArticleServiceImpl implements ArticleService {


    private final ArticleDAO articleDAO;
    private final XSSGuard xssGuard = new XSSGuard();

    @Value("${articles.small.show.size}")
    private int smallArticlesSize;

    @Value("${articles.common.show.size}")
    private int commonArticlesSize;

    public ArticleServiceImpl(@Autowired ArticleDAO articleDAO){
        this.articleDAO = articleDAO;
    }

    /**
     * This methods have a async invoke because
     * converting an image cat take a long time
     * */
    @Async
    @Override
    public Future<Long> createArticle(ThreeImagesPathDTO mainImages, String title, String subtitle, String content, List<Long> tagsStr, long userId){

        String guardedText = xssGuard.replaceScript(content);

        List<String> imagesPath = getMainImages(mainImages);
        if(imagesPath.size() != 3)
            return new AsyncResult<>(0L);

       return new AsyncResult<>(articleDAO.createArticle(imagesPath,title, subtitle, guardedText, tagsStr, userId));
    }

    private List<String> getMainImages(ThreeImagesPathDTO mainImages){

        if(mainImages.getSmall() == null || mainImages.getSmall().equals("")
                || mainImages.getMiddle() == null || mainImages.getMiddle().equals("")
                || mainImages.getLarge() == null || mainImages.getLarge().equals("")){
            return Collections.emptyList();
        }

        return Arrays.asList(mainImages.getSmall(), mainImages.getMiddle(), mainImages.getLarge());
    }

    @Async
    @Override
    public Future<Boolean> updateArticle(long id, ThreeImagesPathDTO threeMainImages, String title, String subtitle, String content, List<Long> tagsStr){

        String guardedText = xssGuard.replaceScript(content);

        List<String> mainImages = getMainImages(threeMainImages);
        if(mainImages.size() != 3)
            return new AsyncResult<>(false);


        return new AsyncResult<>(articleDAO.updateArticle(id, mainImages,title, subtitle, guardedText,  tagsStr)) ;
    }


    @Override
    public List<CommonArticleTransfer> getMiddleArticles(int start, String type, int sort){


        return articleDAO.getArticles(start, commonArticlesSize, orderField(type), sort);
    }


    @Override
    public FullArticleTransfer getArticleByID(long id){

        return articleDAO.getArticleByID(id);
    }

    @Override
    public List<CommonArticleTransfer> findArticles(int start, String search, String type, int sort){


        List<CommonArticleTransfer> rs = articleDAO.findArticles(start,commonArticlesSize ,search, orderField(type), sort);


        return rs;
    }

    @Override
    public List<SmallArticleTransfer> findSmallArticles(int start, String search, String type, int sort){

        List<SmallArticleTransfer> rs = articleDAO.findSmallArticles(start, smallArticlesSize, search, orderField(type), sort);

        return rs;
    }

    @Override
    public List<SmallArticleTransfer> getSmallArticles(int start, String type, int sort) {

        return articleDAO.getSmallArticles(start, smallArticlesSize, orderField(type), sort);
    }

    @Override
    public List<SmallArticleTransfer> getPopularArticles(int start){

       return articleDAO.getSmallArticles(start, smallArticlesSize, "rating", 1);
    }

    @Override
    public void addComment(long articleId, String text, long userId) {

        String securedText = xssGuard.replaceScript(text);

        articleDAO.addComment(articleId, securedText, userId);
    }

    @Override
    public void updateComment(long commentId, String comment) {

        String securedText = xssGuard.replaceScript(comment);

        articleDAO.updateComment(commentId, securedText);
    }

    @Override
    public List<SmallArticleTransfer> getArticlesByUser(int start, long userId, String type, int sort) {
        return articleDAO.getSmallArticlesByUser(start, smallArticlesSize, userId, orderField(type), sort);
    }

    private String orderField(String type){
        switch (type) {
            case "rating":
                return type;

            case "date":
                return "createDate";

            default:
                return "rating";
        }
    }
}
