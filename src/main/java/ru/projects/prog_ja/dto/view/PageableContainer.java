package ru.projects.prog_ja.dto.view;

import java.util.List;

public class PageableContainer {

    private final List list;
    private final PagesDTO pages;

    public PageableContainer(List list, PagesDTO pages) {
        this.list = list;
        this.pages = pages;
    }

    public List getList() {
        return this.list;
    }

    public PagesDTO getPages() {
        return this.pages;
    }
}
