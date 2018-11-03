package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.view.ThreeImagesPathDTO;
import ru.projects.prog_ja.exceptions.BadRequestException;

import java.util.List;

public interface ArticleWriteService {

    FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg,
                                      String title, String subtitle, String content, List<Long> tagsStr, long userId);

    boolean updateArticle(long id, String smallImg, String middleImg, String largeImg,
                          String title, String subtitle, String content, List<Long> tagsStr, long userId);

    boolean changeRate(long commentId, int rate, long userId);

    CommonCommentTransfer addComment(long articleId, String text, long userId);

    boolean updateComment(long commentId, String text, long userId);

    boolean updateCommentRate(long commentId, int rate, long userId);

    boolean removeComment(long commentId, long userId);
}
