package ru.projects.prog_ja.dto.view;

public class ThreeImagesPathDTO {

    private String smallImagePath;
    private String middleImagePath;
    private String largeImagePath;

    public ThreeImagesPathDTO() {
    }

    public ThreeImagesPathDTO(String smallImagePath, String middleImagePath, String largeImagePath) {
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
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
}
