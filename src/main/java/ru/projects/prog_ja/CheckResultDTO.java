package ru.projects.prog_ja;

public class CheckResultDTO {

    private boolean right;

    public CheckResultDTO() {
    }

    public CheckResultDTO(boolean right) {
        this.right = right;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
