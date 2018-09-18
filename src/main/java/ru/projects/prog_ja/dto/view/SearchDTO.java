package ru.projects.prog_ja.dto.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchDTO {

    private int start = 0;

    @Pattern(regexp = "^[A-z | А-я | 1-9]+$")
    @Size(min = 1, max = 100)
    private String search;

    private String type;
    private int sort;

    @NotNull
    private String objectSize;

    public SearchDTO() {
    }

    public SearchDTO(int start, String search, String type, int sort, String objectSize) {
        this.start = start;
        this.search = search;
        this.type = type;
        this.sort = sort;
        this.objectSize = objectSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getObjectSize() {
        return objectSize;
    }

    public void setObjectSize(String objectSize) {
        this.objectSize = objectSize;
    }
}
