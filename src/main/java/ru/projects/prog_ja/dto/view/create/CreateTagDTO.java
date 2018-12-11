package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateTagDTO {

    @Size(min = 3, max = 32)
    @Pattern(regexp = "^[А-я|\\s|\\w]+$")
    private String name;

    @Size(min = 100, max = 1000)
    @Pattern(regexp = "^[А-я|Ё|ё|\\w|\\d|\\s|\\p{Punct}|:|,|\n]+$")
    private String description;

    @Size(max = 7, min = 7)
    @Pattern(regexp = "^#[a-f|A-F|0-9]+$")
    private String color;

    public CreateTagDTO() {
    }

    public CreateTagDTO(long user_id, String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
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
