package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface ArticleReadService {

    List<CommonArticleTransfer> getMiddleArticles(int start, String type, String sort);

    FullArticleTransfer getArticleByID(long id);

    List<CommonArticleTransfer> findArticles(int start, String search, String type, String sort);

    List<SmallArticleTransfer> findSmallArticles(int start, String search, String type, String sort);

    List<SmallArticleTransfer> getSmallArticles(int start, String type, String sort);

    List<CommonArticleTransfer> getArticles(int start, String query, String type, String sort);

    List<CommonArticleTransfer> getDefaultArticles(int start, String type, String sort);

    List<SmallArticleTransfer> getPopularArticles(int start);

    BySomethingContainer getArticlesByUser(int start, String size, long userId, String type, String sort);

    BySomethingContainer getArticlesByTag(int start, String size, long tagId, String type, String sort);
}
