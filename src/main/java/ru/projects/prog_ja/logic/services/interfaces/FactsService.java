package ru.projects.prog_ja.logic.services.interfaces;

import ru.projects.prog_ja.dto.commons.CommonFactTransfer;

import java.util.List;

public interface FactsService {

    CommonFactTransfer getRandomFact();

    CommonFactTransfer getFactByTags(List<Long> tags);

    CommonFactTransfer getFactByTag(long id);

    CommonFactTransfer createFact(String text, List<Long> tags, long userId);

    List<CommonFactTransfer> getFactsByTag(int start, long tagID, String type, int sort);
}
