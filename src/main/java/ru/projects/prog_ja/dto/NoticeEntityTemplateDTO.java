package ru.projects.prog_ja.dto;

public class NoticeEntityTemplateDTO {

    private final long id;
    private final String title;


    public NoticeEntityTemplateDTO(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}
