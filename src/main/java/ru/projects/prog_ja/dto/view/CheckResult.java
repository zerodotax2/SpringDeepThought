package ru.projects.prog_ja.dto.view;

public class CheckResult {

    private boolean right;

    public CheckResult() {
    }

    public CheckResult(boolean right) {
        this.right = right;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
