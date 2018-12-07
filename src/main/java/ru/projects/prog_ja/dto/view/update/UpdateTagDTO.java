package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateTagDTO {

    @NotNull
    private long tagId;

    @NotNull
    @Size(min = 3, max = 32)
    @Pattern(regexp = "^[А-я|\\s|\\w|\\d]+$")
    private String name;

    @NotNull
    @Size(min = 100, max = 1000)
    @Pattern(regexp = "^[А-я|ё|\\w|\\s|\\d|\\p{Punct}]+$")
    private String description;

    @NotNull
    @Size(max = 7, min = 7)
    @Pattern(regexp = "^#[a-f|A-F|0-9]+$")
    private String color;

    public UpdateTagDTO() {
    }

    public UpdateTagDTO(long tagId, String name, String description, String color) {
        this.tagId = tagId;
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

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
}
