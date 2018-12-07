package ru.projects.prog_ja.dto.auth;

public class MailHelper {

    private final String name;
    private final String path;

    public MailHelper(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
