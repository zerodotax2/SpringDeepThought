package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullArticleTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;

import java.util.List;

public interface ArticleReadService {

    PageableContainer getMiddleArticles(String page, String type, String sort);

    FullArticleTransfer getArticleByID(long id, UserDTO userDTO);

    PageableContainer findArticles(String page, String search, String type, String sort);

    PageableContainer findSmallArticles(String page, String search, String type, String sort);

    PageableContainer getSmallArticles(String page, String type, String sort);

    PageableContainer getArticles(String page, String query, String type, String sort);

    PageableContainer getDefaultArticles(String page, String type, String sort);

    List<SmallArticleTransfer> getPopularArticles();

    PageableContainer getSmallArticlesByUser(String page, String size, long userId, String query, String type, String sort);

    PageableContainer getCommonArticlesByUser(String page, String size, long userId, String query, String type, String sort);

    PageableContainer getCommonArticlesByTag(String page, String size, long tagId,String query, String type, String sort);

    PageableContainer getSmallArticlesByTag(String page, String size, long tagId,String query, String type, String sort);
}
