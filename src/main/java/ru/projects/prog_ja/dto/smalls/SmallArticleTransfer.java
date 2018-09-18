package ru.projects.prog_ja.dto.smalls;

import ru.projects.prog_ja.dto.TagsContainer;

public class SmallArticleTransfer extends TagsContainer {

    protected String title;
    protected String articleImage;
    protected long rating;

    public SmallArticleTransfer(long id, String title, String articleImage, long rating) {
        super(id);
        this.title = title;
        this.articleImage = articleImage;
        this.rating = rating;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Small post ").append(id).append(": \n");
        for(SmallTagTransfer tag : tags){
            sb.append(tag);
        }
        return sb.toString();
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

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
