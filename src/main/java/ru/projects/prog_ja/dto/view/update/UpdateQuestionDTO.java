package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.*;
import java.util.List;

public class UpdateQuestionDTO {

    @NotNull
    private long questionId;

    @NotNull
    @Min(value = 5, message = "Заголовок не может быть меньше 5 символов")
    @Max(value = 50, message = "Максимальная длина заголовка - 50 символов")
    @Pattern(regexp = "^[\\w|\\s]+$", message = "Неверный формат названия")
    private String title;

    @NotNull
    private String htmlContent;

    @NotNull
    @Size(min = 3, max = 5)
    private List<Long> tags;

    public UpdateQuestionDTO() {
    }

    public UpdateQuestionDTO(long questionId, String title, String htmlContent, List<Long> tags) {
        this.questionId = questionId;
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

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}
