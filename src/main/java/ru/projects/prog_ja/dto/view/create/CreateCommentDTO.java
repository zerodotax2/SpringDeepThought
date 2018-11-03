package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateCommentDTO {

    @NotNull
    private long id;

    @NotNull
    @Size(min = 5, max = 1000)
    @Pattern(regexp = "^[\\w|\\s]+$")
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
