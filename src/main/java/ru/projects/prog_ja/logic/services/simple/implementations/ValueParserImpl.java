package ru.projects.prog_ja.logic.services.simple.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.view.PagesDTO;
import ru.projects.prog_ja.dto.view.ProblemDifficult;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;

@Service
@Scope("prototype")
public class ValueParserImpl implements ValuesParser {

    private static int maxEntitiesSize;

    @Override
    public int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
    }

    @Override
    public int getPage(String page){
        if(page!=null && RegexUtil.intNumber(page).matches())
            return Integer.parseInt(page);

        return 1;
    }

    @Override
    public String getQuery(String q){
        if(q != null && RegexUtil.string(q).matches())
            return q;

        return null;
    }

    @Override
    public int getSort(String sort){
        return "0".equals(sort) ? 0 : 1;
    }

    @Override
    public PagesDTO getPages(int page, long count, int size) {

        int pages = (int) Math.ceil((double) count / size);

        int first, last;
        if(page <= 2){
            first = 1; last = Math.min(pages, 3);
        }else if(page >= pages-1){
            first = pages - 2; last = pages;
        }else{
            first = page - 1; last = page + 1;
        }

        return new PagesDTO(first, last, page, pages);
    }


    @Override
    public ProblemDifficult getDifficult(String d){
        if(d == null)
            return null;
        switch (d) {
            case "HELL":
                return ProblemDifficult.HELL;
            case "HARD":
                return ProblemDifficult.HARD;
            case "NORMAL":
                return ProblemDifficult.NORMAL;
            default:
                return ProblemDifficult.EASY;
        }
    }

    @Value("${entities.max.size}")
    public void setMaxEntitiesSize(int size) {
        maxEntitiesSize = size;
    }
}
