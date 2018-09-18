package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.*;
import java.util.List;

public class CreateArticleDTO {

    @NotNull
    private String smallImagePath;

    @NotNull
    private String middleImagePath;

    @NotNull
    private String largeImagePath;

    @Min(value = 10, message = "Минимальная длина заголовка - 10 символов")
    @Max(value = 100, message = "Максимальная длина заголовка - 100 символов")
    @NotNull(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[A-z|1-9|А-я]+$", message = "Заголовок может включать только следующие символы (A-z, А-я, 1-9)")
    private String title;

    @Min(value = 10, message = "Минимальная длина подзаголовка - 10 символов")
    @Max(value = 100, message = "Максимальная длина подзаголовка - 100 символов")
    @NotNull(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[A-z|1-9|А-я]+$", message = "Подзаголовок может включать только следующие символы (A-z, А-я, 1-9)")
    private String subtitle;

    @NotNull(message = "Поле не может быть пустым")
    private String htmlContent;

    @Min(value = 17, message = "Минимальное кол-во тегов - 3")
    @Max(value = 29, message = "Максимальное кол-во тегов - 5")
    @NotNull(message = "Поле не может быть пустым")
    private String tagsSTR;

    @NotNull
    @Pattern(regexp = "^\\d+$", message = "Неправильный id пользователя")
    private long userId;

    public CreateArticleDTO(){}

    public CreateArticleDTO(String smallImagePath, String middleImagePath, String largeImagePath, String title, String subtitle, String htmlContent, String tagsSTR, long userId) {
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
        this.title = title;
        this.subtitle = subtitle;
        this.htmlContent = htmlContent;
        this.tagsSTR = tagsSTR;
        this.userId = userId;
    }

    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    public String getMiddleImagePath() {
        return middleImagePath;
    }

    public void setMiddleImagePath(String middleImagePath) {
        this.middleImagePath = middleImagePath;
    }

    public String getLargeImagePath() {
        return largeImagePath;
    }

    public void setLargeImagePath(String largeImagePath) {
        this.largeImagePath = largeImagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTagsSTR() {
        return tagsSTR;
    }

    public void setTagsSTR(String tagsSTR) {
        this.tagsSTR = tagsSTR;
    }
}
