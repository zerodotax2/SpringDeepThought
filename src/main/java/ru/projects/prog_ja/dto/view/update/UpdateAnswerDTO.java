package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.NotNull;

public class UpdateAnswerDTO {

    @NotNull
    private long id;

    @NotNull
    private String htmlContent;

    public UpdateAnswerDTO() {
    }

    public UpdateAnswerDTO(long id, String htmlContent) {
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
