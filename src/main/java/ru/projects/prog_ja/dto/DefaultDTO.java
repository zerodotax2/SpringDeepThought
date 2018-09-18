package ru.projects.prog_ja.dto;

public class DefaultDTO {

    protected long id;

    public DefaultDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
