package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateTagDTO {

    @NotNull
    private long user_id;

    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    @NotNull
    @Size(min = 3, max = 32)
    private String description;

    private String color;

    public CreateTagDTO() {
    }

    public CreateTagDTO(long user_id, String name, String description, String color) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
