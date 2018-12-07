package ru.projects.prog_ja.logic.services.simple.interfaces;

import ru.projects.prog_ja.dto.view.PagesDTO;
import ru.projects.prog_ja.dto.view.ProblemDifficult;

public interface ValuesParser {

    int getSort(String sort);

    int getPage(String page);

    int getSize(String size);

    String getQuery(String q);

    PagesDTO getPages(int page, long count, int size);

    ProblemDifficult getDifficult(String diff);
}
