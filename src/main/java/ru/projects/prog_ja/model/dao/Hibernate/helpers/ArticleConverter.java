package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.entity.articles.ArticleComments;
import ru.projects.prog_ja.model.entity.articles.ArticleContent;
import ru.projects.prog_ja.model.entity.articles.ArticleInfo;
import ru.projects.prog_ja.model.entity.articles.ArticlesTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Service
@Scope("prototype")
public class ArticleConverter {

    public FullArticleTransfer fullArticle(ArticleInfo article, ArticleContent content, Set<ArticleComments> comments, Set<ArticlesTags> tags, UserInfo user){

        FullArticleTransfer fullArticleTransfer = new FullArticleTransfer(
                article.getArticleId(), article.getTitle(), article.getLargeImagePath(), article.getCreateDate(), article.getViews(), article.getRating(),
                new SmallUserTransfer(user.getUserId(), user.getLogin(), user.getSmallImagePath(), user.getRating()),
                content.getSubtitle(), content.getHtmlContent()
        );

        Set<CommonCommentTransfer> commentsTransfers = new TreeSet<>();
        for(ArticleComments comment : comments){
            UserInfo commenter = comment.getUserInfo();
            commentsTransfers.add(new CommonCommentTransfer(
                    comment.getPostCommentId(), comment.getComment(), comment.getRating(), comment.getCreateDate(),
                    new SmallUserTransfer(commenter.getUserId(), commenter.getLogin(), commenter.getSmallImagePath(), commenter.getRating())
            ));
        }
        fullArticleTransfer.setComments(commentsTransfers);

        Set<SmallTagTransfer> tagTransfers = new HashSet<>();
        for(ArticlesTags tag1 : tags){
            Tags tag = tag1.getTagId();
            tagTransfers.add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
        }
        fullArticleTransfer.setTags(tagTransfers);

        return fullArticleTransfer;
    }
}
