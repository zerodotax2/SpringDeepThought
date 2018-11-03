package ru.projects.prog_ja.dto.view;

import javax.validation.constraints.NotNull;

public class RemoveDTO {

    @NotNull
    private long id;

    public RemoveDTO() {
    }

    public RemoveDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
