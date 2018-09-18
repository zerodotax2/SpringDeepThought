package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreateQuestionDTO {

    @NotNull
    @Min(value = 5, message = "Заголовок не может быть меньше 5 символов")
    @Max(value = 50, message = "Максимальная длина заголовка - 50 символов")
    @Pattern(regexp = "^[A-z|1-9|\\-|А-я]+$", message = "Неверный формат названия")
    private String title;

    @NotNull
    private String htmlContent;

    @NotNull
    @Min(value = 17, message = "Должно быть не меньше 3-х тегов")
    @Max(value = 29, message = "Максимальное кол-во тегов - 5")
    private String tagsSTR;

    @NotNull
    @Pattern(regexp = "^\\d+$")
    private long userId;

    public CreateQuestionDTO() {
    }

    public CreateQuestionDTO(String title, String htmlContent, String tagsSTR, long userId) {
        this.title = title;
        this.htmlContent = htmlContent;
        this.tagsSTR = tagsSTR;
        this.userId = userId;
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

    public String getTagsSTR() {
        return tagsSTR;
    }

    public void setTagsSTR(String tagsSTR) {
        this.tagsSTR = tagsSTR;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
