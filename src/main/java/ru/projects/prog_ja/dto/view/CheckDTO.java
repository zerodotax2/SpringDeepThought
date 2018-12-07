package ru.projects.prog_ja.dto.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CheckDTO {

    @NotNull
    private long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[А-я| |0-9|A-z|\\-|_]+$")
    private String value;

    public CheckDTO() {
    }

    public CheckDTO(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
