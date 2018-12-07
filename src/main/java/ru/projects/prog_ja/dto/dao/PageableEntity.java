package ru.projects.prog_ja.dto.dao;

import java.util.List;

public class PageableEntity {

    private final List list;
    private final long count;

    public PageableEntity(List list, long count) {
        this.list = list;
        this.count = count;
    }

    @Override
    public String toString(){
       return  "===================================\n"
                +"Pageable: \n"
                +"count: " + count + "\n"
                +"list: \n" + list +
        "====================================";
    }

    public List getList() {
        return list;
    }

    public long getCount() {
        return count;
    }
}
