package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateArticleDTO {

    @NotNull
    private long articleId;

    @NotNull
    @Pattern( regexp = "^.+?\\.(png|gif|jpe?g)$")
    private String smallImagePath;

    @NotNull
    @Pattern( regexp = "^.+?\\.(png|gif|jpe?g)$")
    private String middleImagePath;

    @NotNull
    @Pattern( regexp = "^.+?\\.(png|gif|jpe?g)$")
    private String largeImagePath;

    @Size(min = 10, max = 100)
    @NotNull(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[А-я|Ё|ё|\\w|\\d|\\s|\\p{Punct}|:|,]+$", message = "Заголовок может включать только следующие символы (A-z, А-я, 1-9)")
    private String title;

    @Size(min = 10, max = 100)
    @NotNull(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[А-я|Ё|ё|\\w|\\d|\\s|\\p{Punct}|:|,]+$", message = "Подзаголовок может включать только следующие символы (A-z, А-я, 1-9)")
    private String subtitle;

    @NotNull(message = "Поле не может быть пустым")
    private String htmlContent;

    @Size(min = 2, max = 5)
    @NotNull(message = "Поле не может быть пустым")
    private List<Long> tags;


    public UpdateArticleDTO(){}

    public UpdateArticleDTO(long articleId, String smallImagePath, String middleImagePath, String largeImagePath, String title, String subtitle, String htmlContent, List<Long> tags) {
        this.articleId = articleId;
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
        this.title = title;
        this.subtitle = subtitle;
        this.htmlContent = htmlContent;
        this.tags = tags;
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

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }
}
