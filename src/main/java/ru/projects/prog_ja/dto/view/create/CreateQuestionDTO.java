package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateQuestionDTO {

    @NotNull
    @Size(min = 5, max = 50)
    @Pattern(regexp = "^[А-я|ё|\\w|\\s|\\d|\\p{Punct}]+$", message = "Неверный формат названия")
    private String title;

    @NotNull
    private String htmlContent;

    @NotNull
    @Size(min = 2, max = 5)
    private List<Long> tags;

    public CreateQuestionDTO() {
    }

    public CreateQuestionDTO(String title, String htmlContent, List<Long> tags) {
        this.title = title;
        this.htmlContent = htmlContent;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }
}
