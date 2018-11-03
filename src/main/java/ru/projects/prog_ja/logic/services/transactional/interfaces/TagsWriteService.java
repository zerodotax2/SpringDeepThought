package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonTagTransfer;

public interface TagsWriteService {

    CommonTagTransfer createTag(String name, String description, String color, long userId);

    boolean updateTag(long tagId, String name, String description, String color, long userId);

    boolean removeTag(long tagId, long userId);

}
