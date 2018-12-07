package ru.projects.prog_ja.dto.view;

public class ViewArticleTransfer {

    private long id;
    private String title;
    private String articleImage;

    public ViewArticleTransfer(long id, String title, String articleImage) {
        this.id = id;
        this.title = title;
        this.articleImage = articleImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }
}
