package ru.projects.prog_ja.dto.auth;

import javax.validation.constraints.NotNull;

public class ExistsDTO {

    @NotNull
    private String type;

    @NotNull
    private String value;

    public ExistsDTO() {
    }

    public ExistsDTO(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
