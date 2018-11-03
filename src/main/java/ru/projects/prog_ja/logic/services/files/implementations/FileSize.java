package ru.projects.prog_ja.logic.services.files.implementations;

public class FileSize {

    private final int width;
    private final int height;
    private final String name;

    public FileSize(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }
}
