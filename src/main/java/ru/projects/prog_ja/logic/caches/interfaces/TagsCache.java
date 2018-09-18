package ru.projects.prog_ja.logic.caches.interfaces;

import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.List;


public interface TagsCache {

    void putTag(CommonTagTransfer tag);

    CommonTagTransfer getCommonTagByID(long id);

    List<SmallTagTransfer> getPopularTags();

    void deleteTag(long tagId);
}
