package ru.projects.prog_ja.dto.view;

public class RenderDTO {

    private int start = 0;
    private String type;
    private int sort;
    private String objectSize;

    public RenderDTO() {
    }

    public RenderDTO(int start, String type, int sort, String objectSize) {
        this.start = start;
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
