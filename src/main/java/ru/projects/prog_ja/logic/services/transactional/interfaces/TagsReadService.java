package ru.projects.prog_ja.logic.services.transactional.interfaces;


import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;

import java.util.List;

public interface TagsReadService {

    List<SmallTagTransfer> getPopularTags();

    String getName(long tagId);

    PageableContainer getCommonTags(String page, String type, String sort);

    PageableContainer findCommonTags(String page, String search, String type, String sort);

    PageableContainer getSmallTags(String page, String type, String sort);

    PageableContainer findSmallTags(String page, String search, String type, String sort);

    FullTagTransfer getFullTag(long id);

    PageableContainer getTags(String page, String query, String type, String sort);

    CommonTagTransfer getCommonTag(long id);

    List<SmallTagTransfer> getTagsByPrefix(String prefix);

    PageableContainer getTagsByUser(String page, String size, long userId, String q, String type, String sort);
}
