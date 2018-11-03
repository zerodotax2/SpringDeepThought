package ru.projects.prog_ja.logic.services.transactional.interfaces;


import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.List;

public interface TagsReadService {

    List<SmallTagTransfer> getPopularTags();

    List<CommonTagTransfer> getCommonTags(int start, String type, String sort);

    List<CommonTagTransfer> findCommonTags(int start, String search, String type, String sort);

    List<SmallTagTransfer> getSmallTags(int start, String type, String sort);

    List<SmallTagTransfer> findSmallTags(int start, String search, String type, String sort);

    FullTagTransfer getFullTag(long id);

    List<CommonTagTransfer> getTags(int start, String query, String type, String sort);

    CommonTagTransfer getCommonTag(long id);
}
