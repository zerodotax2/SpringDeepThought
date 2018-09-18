package ru.projects.prog_ja.dto.view;

public class ThreeImagesPathDTO {

    private String small;
    private String middle;
    private String large;

    public ThreeImagesPathDTO() {
    }

    public ThreeImagesPathDTO(String small, String middle, String large) {
        this.small = small;
        this.middle = middle;
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
