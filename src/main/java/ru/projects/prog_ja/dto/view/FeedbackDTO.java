package ru.projects.prog_ja.dto.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FeedbackDTO {

    private long id;

    @NotNull
    @Size(min = 5, max = 500)
    @Pattern(regexp = "^[А-я|ё|\\w|\\s|\\d|\\p{Punct}]+$")
    private String text;

    public FeedbackDTO() {
    }

    public FeedbackDTO(long id,String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
