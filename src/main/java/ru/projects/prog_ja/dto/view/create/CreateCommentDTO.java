package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateCommentDTO {

    private long id;

    @Size(min = 5, max = 1000)
    @Pattern(regexp = "^[А-я|Ё|ё|\\w|\\d|\\s|\\p{Punct}|:|,|\n]+$")
    private String text;

    public CreateCommentDTO(){ }

    public CreateCommentDTO(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
