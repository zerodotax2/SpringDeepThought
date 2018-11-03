package ru.projects.prog_ja.dto.view;

import javax.validation.constraints.NotNull;

public class UpdateDTO {
    
    @NotNull
    private String value;
    
    public UpdateDTO(){}

    public UpdateDTO(@NotNull String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
