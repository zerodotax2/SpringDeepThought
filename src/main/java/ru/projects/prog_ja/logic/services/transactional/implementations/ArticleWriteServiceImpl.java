package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleWriteService;
import ru.projects.prog_ja.model.dao.ArticleDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ArticleWriteServiceImpl implements ArticleWriteService {

    private final ArticleDAO articleDAO;
    private XSSGuardService xssGuardService;

    @Autowired
    public ArticleWriteServiceImpl(ArticleDAO articleDAO,
                                   XSSGuardService xssGuardService){
        this.articleDAO = articleDAO;
        this.xssGuardService = xssGuardService;
    }

    @Override
    public FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg, String title, String subtitle, String content, List<Long> tagsStr, long userId){

        String guardedText = xssGuardService.replaceScript(content);
        if(ifNullImages(smallImg, middleImg, largeImg)){
            return null;
        }

        return articleDAO.createArticle(smallImg,middleImg,largeImg,title, subtitle, guardedText, tagsStr, userId);
    }

    @Override
    public boolean updateArticle(long id, String smallImg, String middleImg, String largeImg, String title, String subtitle, String content, List<Long> tagsStr, long userId){

        String guardedText = xssGuardService.replaceScript(content);

        if(ifNullImages(smallImg, middleImg, largeImg)){
            return false;
        }

        return articleDAO.updateArticle(id, smallImg,middleImg,largeImg,title, subtitle, guardedText,  tagsStr, userId);
    }

    private boolean ifNullImages(String smallImg, String middleImg, String largeImg){

        return  checkImage(smallImg)
                && checkImage(middleImg)
                && checkImage(largeImg);
    }

    private boolean checkImage(String img){
        return img != null
                && !img.equals("")
                && img.matches(".+\\.(png|jpe?g|svg|gif)$");
    }

    @Override
    public CommonCommentTransfer addComment(long articleId, String text, long userId) {

        return articleDAO.addComment(articleId, text, userId);
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId) {

        return articleDAO.updateComment(commentId, comment, userId);
    }

    @Override
    public boolean updateCommentRate(long commentId, int rate, long userId) {

        return articleDAO.changeCommentRate(commentId, getRate(rate), userId);
    }

    @Override
    public boolean removeComment(long commentId, long userId){

        return articleDAO.removeComment(commentId, userId);
    }

    @Override
    public boolean changeRate(long commentId, int rate, long userId){

        return articleDAO.changeCommentRate(commentId, getRate(rate), userId);
    }

    private int getRate(int rate){
        return rate > 0 ? 1 : -1;
    }

}
