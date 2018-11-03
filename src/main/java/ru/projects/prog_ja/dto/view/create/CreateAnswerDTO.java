package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;

public class CreateAnswerDTO {

    @NotNull
    private long id;

    @NotNull
    private String htmlContent;

    public CreateAnswerDTO() {
    }

    public CreateAnswerDTO(long id, String htmlContent) {
        this.id = id;
        this.htmlContent = htmlContent;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
