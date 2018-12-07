package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.exceptions.RepeatVotedException;
import ru.projects.prog_ja.logic.queues.notifications.services.ArticleNoticeService;
import ru.projects.prog_ja.logic.queues.stats.services.TagCounter;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.logic.services.simple.interfaces.XSSGuardService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.ArticleWriteService;
import ru.projects.prog_ja.logic.services.transactional.interfaces.RatingService;
import ru.projects.prog_ja.model.dao.ArticleDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class ArticleWriteServiceImpl implements ArticleWriteService {

    private final ArticleDAO articleDAO;
    private final ArticleNoticeService articleNoticeService;
    private final RatingService ratingService;
    private final TagCounter tagCounter;
    private final UserCounter userCounter;
    private XSSGuardService xssGuardService;


    private static int RATE_article_create;
    private static int RATE_article_comment;
    private static int RATE_article_rate;

    @Autowired
    public ArticleWriteServiceImpl(ArticleDAO articleDAO,
                                   XSSGuardService xssGuardService,
                                   ArticleNoticeService articleNoticeService,
                                   RatingService ratingService,
                                   TagCounter tagCounter,
                                   UserCounter userCounter){
        this.articleDAO = articleDAO;
        this.xssGuardService = xssGuardService;
        this.articleNoticeService = articleNoticeService;
        this.ratingService = ratingService;
        this.tagCounter = tagCounter;
        this.userCounter = userCounter;
    }

    @Override
    public FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg, String title, String subtitle, String content, List<Long> tagsStr, long userId){

        String guardedText = xssGuardService.replaceScript(content);

        FullArticleTransfer article
                = articleDAO.createArticle(smallImg,middleImg,largeImg,title, subtitle, guardedText, tagsStr, userId);
        if(article != null){

            for (long tagID : tagsStr){
                tagCounter.incrementArticles(tagID, 1);
            }
            userCounter.incrementArticles(userId, 1);

            ratingService.updateUserRate(userId, RATE_article_create);
        }

        return article;
    }

    @Override
    public boolean updateArticle(long id, String smallImg, String middleImg, String largeImg, String title, String subtitle, String content, List<Long> tagsStr, long userId){

        String guardedText = xssGuardService.replaceScript(content);
        List<Long> oldTags = articleDAO.getTagsByArticle(id);

        if(articleDAO.updateArticle(id, smallImg,middleImg,largeImg,title, subtitle, guardedText,  tagsStr, userId)){

            for(long tagId : oldTags){
                tagCounter.incrementArticles(tagId, -1);
            }
            for(long tagId : tagsStr){
                tagCounter.incrementArticles(tagId, 1);
            }

            return true;
        }
        return false;
    }


    @Override
    public CommonCommentTransfer addComment(long articleId, String text, long userId) {

        CommonCommentTransfer commonCommentTransfer = articleDAO.addComment(articleId, text, userId);
        if(commonCommentTransfer != null){
            userCounter.incrementComments(userId, 1);
            articleNoticeService.addCommentNotice(articleId, userId);
            ratingService.updateArticleOwnerRate(articleId, RATE_article_comment, userId);
        }
        return commonCommentTransfer;
    }

    @Override
    public boolean updateComment(long commentId, String comment, long userId){

        return articleDAO.updateComment(commentId, comment, userId);
    }

    @Override
    public boolean updateCommentRate(long commentId, int rate, long userId) throws RepeatVotedException {

        if(articleDAO.isArticleCommentVoted(commentId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(articleDAO.changeCommentRate(commentId, parsedRate, userId)){
            articleNoticeService.addCommentRateNotice(commentId, userId);
            ratingService.updateArticleCommentOwnerRate(commentId, parsedRate, userId);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeComment(long commentId, long userId){

        if(articleDAO.removeComment(commentId, userId)){

            userCounter.incrementComments(userId, -1);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeRate(long articleId, int rate, long userId) throws RepeatVotedException{

        if(articleDAO.isArticleVoted(articleId, userId))
            throw new RepeatVotedException();

        int parsedRate = getRate(rate);
        if(articleDAO.changeRate(articleId, parsedRate, userId)){

            articleNoticeService.addRateNotice(articleId, userId);
            ratingService.updateArticleOwnerRate(articleId, parsedRate, userId);
            return true;
        }

        return false;
    }

    private int getRate(int rate){
        return rate > 0 ? 1 : -1;
    }

    @Value("${article.create}")
    private void setRATE_article_create(int rate){
        RATE_article_create = rate;
    }
    @Value("${article.comment}")
    private void setRATE_article_comment(int rate){
        RATE_article_comment = rate;
    }
    @Value("${article.rate}")
    private void setRATE_article_rate(int rate){
        RATE_article_rate = rate;
    }
}
