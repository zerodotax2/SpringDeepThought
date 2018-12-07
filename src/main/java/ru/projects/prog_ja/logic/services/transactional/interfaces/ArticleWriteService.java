package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.exceptions.RepeatVotedException;

import java.util.List;

public interface ArticleWriteService {

    FullArticleTransfer createArticle(String smallImg, String middleImg, String largeImg,
                                      String title, String subtitle, String content, List<Long> tagsStr, long userId);

    boolean updateArticle(long id, String smallImg, String middleImg, String largeImg,
                          String title, String subtitle, String content, List<Long> tagsStr, long userId);

    boolean changeRate(long articleId, int rate, long userId) throws RepeatVotedException;

    CommonCommentTransfer addComment(long articleId, String text, long userId);

    boolean updateComment(long commentId, String text, long userId);

    boolean updateCommentRate(long commentId, int rate, long userId) throws RepeatVotedException;

    boolean removeComment(long commentId, long userId);
}
