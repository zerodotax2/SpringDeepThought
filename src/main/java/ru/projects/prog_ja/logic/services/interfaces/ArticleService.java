package ru.projects.prog_ja.logic.services.interfaces;

import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface ArticleService {

    Future<Long> createArticle(ThreeImagesPathDTO mainImages, String title, String subtitle, String content, List<Long> tagsStr, long userId);

    Future<Boolean> updateArticle(long id, ThreeImagesPathDTO mainImages, String title, String subtitle, String content, List<Long> tagsStr);

    List<CommonArticleTransfer> getMiddleArticles(int start, String type, int sort);

    FullArticleTransfer getArticleByID(long id);

    List<CommonArticleTransfer> findArticles(int start, String search, String type, int sort);

    List<SmallArticleTransfer> findSmallArticles(int start, String search, String type, int sort);

    List<SmallArticleTransfer> getSmallArticles(int start, String type, int sort);

    List<SmallArticleTransfer> getPopularArticles(int start);

    void addComment(long articleId, String text, long userId);

    void updateComment(long commentId, String comment);

    List<SmallArticleTransfer> getArticlesByUser(int start, long userId, String type, int sort);
}
