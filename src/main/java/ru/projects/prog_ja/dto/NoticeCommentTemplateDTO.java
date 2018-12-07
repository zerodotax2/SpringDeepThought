package ru.projects.prog_ja.dto;

public class NoticeCommentTemplateDTO {

    private final long id;
    private final long userId;
    private final String title;


    public NoticeCommentTemplateDTO(long id, long userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    public long getUserId() {
        return userId;
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

}
