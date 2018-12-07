package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.logic.queues.views.ViewsQueue;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleReadService;
import ru.projects.prog_ja.model.dao.ArticleDAO;

import java.util.List;

@Service
@Scope( scopeName = "prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ArticleReadServiceImpl implements ArticleReadService {


    private final ArticleDAO articleDAO;
    private final ValuesParser parser;
    private final ViewsQueue viewsQueue;


    private static int smallArticlesSize;
    private static int commonArticlesSize;


    @Autowired
    public ArticleReadServiceImpl(ArticleDAO articleDAO,
                                  ViewsQueue viewsQueue,
                                  ValuesParser parser){
        this.articleDAO = articleDAO;
        this.viewsQueue = viewsQueue;
        this.parser = parser;
    }


    @Override
    public PageableContainer getMiddleArticles(String page, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                articleDAO.getArticles((parsedPage - 1) * commonArticlesSize, commonArticlesSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonArticlesSize));
    }


    @Override
    public FullArticleTransfer getArticleByID(long id, UserDTO userDTO){

        FullArticleTransfer fullArticleTransfer = articleDAO.getArticleByID(id);

        if(fullArticleTransfer != null && userDTO != null && fullArticleTransfer.getUser().getId() != userDTO.getId()){

            viewsQueue.addArticleView(fullArticleTransfer.getId());
        }

        return fullArticleTransfer;
    }

    @Override
    public PageableContainer findArticles(String page, String query, String type, String sort) {

        if(query == null || !RegexUtil.string(query).matches())
            return getDefaultArticles(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                articleDAO.findArticles((parsedPage - 1) * commonArticlesSize, commonArticlesSize, query, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonArticlesSize));
    }

    @Override
    public PageableContainer findSmallArticles(String page, String search, String type, String sort){

        if(search == null || !RegexUtil.string(search).matches())
            return getSmallArticles(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable
                = articleDAO.findSmallArticles((parsedPage - 1) * commonArticlesSize, smallArticlesSize, search, getOrderField(type), parser.getSort(sort));

        return  new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallArticlesSize));
    }

    @Override
    public PageableContainer getSmallArticles(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                articleDAO.getSmallArticles((parsedPage - 1) * smallArticlesSize, smallArticlesSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallArticlesSize));
    }

    @Override
    public List<SmallArticleTransfer> getPopularArticles() {

        PageableEntity pageable =
                articleDAO.getSmallArticles(0, smallArticlesSize, "rating", 1);

        return pageable.getList();
    }

    @Override
    public PageableContainer getSmallArticlesByUser(String page, String size, long userId, String query, String type, String sort) {


        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = articleDAO.getSmallArticlesByUser((parsedPage - 1) * smallArticlesSize, parsedSize+1,
                userId, parser.getQuery(query), getOrderField(type), parser.getSort(sort));

        return  new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallArticlesSize));
    }

    @Override
    public PageableContainer getSmallArticlesByTag(String page, String size, long tagId, String query, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);

        PageableEntity pageable = articleDAO.getSmallArticlesByTag((parsedPage - 1) * smallArticlesSize, parsedSize+1,
                tagId, parser.getQuery(query), getOrderField(type), parser.getSort(sort));

        return  new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallArticlesSize));
    }

    @Override
    public PageableContainer getCommonArticlesByUser(String page, String size, long userId, String query, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = articleDAO.getCommonArticlesByUser((parsedPage - 1) * commonArticlesSize, parsedSize+1,
                userId, parser.getQuery(query), getOrderField(type), parser.getSort(sort));

        return  new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonArticlesSize));
    }

    @Override
    public PageableContainer getCommonArticlesByTag(String page, String size, long tagId, String query, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable =
                articleDAO.getCommonArticlesByTag((parsedPage-1) * commonArticlesSize, parsedSize+1,
                tagId, parser.getQuery(query), getOrderField(type), parser.getSort(sort));

        return  new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonArticlesSize));
    }

    @Override
    public PageableContainer getArticles(String page, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultArticles(page, type, sort);
        }else{
            return findArticles(page, query, type, sort);
        }
    }

    @Override
    public PageableContainer getDefaultArticles(String page, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                articleDAO.getArticles((parsedPage - 1) * commonArticlesSize, commonArticlesSize,getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonArticlesSize));
    }

    private String getOrderField(String type){

        if(type == null)
            return "rating";

        switch (type) {
            case "rating":
                return type;

            case "date":
                return "createDate";

            default:
                return "rating";
        }
    }

    @Value("${articles.small.show.size}")
    public void setSmallArticlesSize(int size) {
        smallArticlesSize = size;
    }

    @Value("${articles.common.show.size}")
    public void setCommonArticlesSize(int size) {
        commonArticlesSize = size;
    }
}
