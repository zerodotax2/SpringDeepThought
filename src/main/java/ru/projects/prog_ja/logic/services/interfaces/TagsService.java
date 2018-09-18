package ru.projects.prog_ja.logic.services.interfaces;


import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.full.FullTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.List;

public interface TagsService {

    List<SmallTagTransfer> getPopularTags();

    List<CommonTagTransfer> getCommonTags(int start, String type, int sort);

    List<CommonTagTransfer> findCommonTags(int start, String search, String type, int sort);

    List<SmallTagTransfer> getSmallTags(int start, String type, int sort);

    List<SmallTagTransfer> findSmallTags(int start, String search, String type, int sort);

    FullTagTransfer getFullTag(long id);

    CommonTagTransfer createTag(String name, String description, String color, long userId);

    CommonTagTransfer getCommonTag(long id);
}
