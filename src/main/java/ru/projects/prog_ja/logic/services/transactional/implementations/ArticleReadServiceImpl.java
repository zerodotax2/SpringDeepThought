package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.model.dao.ArticleDAO;

import java.util.List;

@Service
@Scope( scopeName = "prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ArticleReadServiceImpl implements ArticleReadService {


    private final ArticleDAO articleDAO;

    @Value("${articles.small.show.size}")
    private static int smallArticlesSize;

    @Value("${articles.common.show.size}")
    private static int commonArticlesSize;

    @Value("{entities.max.size}")
    private static int maxEntitiesSize;


    @Autowired
    public ArticleReadServiceImpl(ArticleDAO articleDAO){
        this.articleDAO = articleDAO;
    }


    @Override
    public List<CommonArticleTransfer> getMiddleArticles(int start, String type, String sort){


        return articleDAO.getArticles(start, commonArticlesSize, getOrderField(type), getSort(sort));
    }


    @Override
    public FullArticleTransfer getArticleByID(long id){

        return articleDAO.getArticleByID(id);
    }

    @Override
    public List<CommonArticleTransfer> findArticles(int start, String query, String type, String sort) {

        if(!query.matches("^[\\w|\\s]$"))
            return getDefaultArticles(start, type, sort);

        return articleDAO.findArticles(start, commonArticlesSize, query, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallArticleTransfer> findSmallArticles(int start, String search, String type, String sort){

        if(!search.matches("^[\\w|\\s]$"))
            return getSmallArticles(start, type, sort);

        List<SmallArticleTransfer> rs = articleDAO.findSmallArticles(start, smallArticlesSize, search, getOrderField(type), getSort(sort));

        return rs;
    }

    @Override
    public List<SmallArticleTransfer> getSmallArticles(int start, String type, String sort) {

        return articleDAO.getSmallArticles(start, smallArticlesSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallArticleTransfer> getPopularArticles(int start) {

        return articleDAO.getSmallArticles(start, smallArticlesSize, "rating", 1);
    }

    @Override
    public BySomethingContainer getArticlesByUser(int start, String size, long userId, String type, String sort) {


        int parsedSize = getSize(size);
        List<SmallArticleTransfer> articles =articleDAO.getSmallArticlesByUser(start, parsedSize+1,
                userId, getOrderField(type), getSort(sort));

        return articles != null ? new BySomethingContainer(articles.size() > parsedSize, articles) : null;
    }

    @Override
    public BySomethingContainer getArticlesByTag(int start, String size, long tagId, String type, String sort) {

        int parsedSize = getSize(size);
        List<SmallArticleTransfer> articles =articleDAO.getSmallArticlesByTag(start, parsedSize+1,
                tagId, getOrderField(type), getSort(sort));

        return articles != null ? new BySomethingContainer(articles.size() > parsedSize, articles) : null;
    }

    @Override
    public List<CommonArticleTransfer> getArticles(int start, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultArticles(start, type, sort);
        }else{
            return findArticles(start, query, type, sort);
        }
    }

    @Override
    public List<CommonArticleTransfer> getDefaultArticles(int start, String type, String sort){

        return articleDAO.getArticles(start, commonArticlesSize,getOrderField(type), getSort(sort));
    }

    private int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
    }

    private int getSort(String sort){
        return "0".equals(sort) ? 0 : 1;
    }

    private String getOrderField(String type){
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
