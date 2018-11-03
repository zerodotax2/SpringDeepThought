package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateFactDTO {

    @NotNull
    @Size(min = 100, max = 1000)
    @Pattern(regexp = "^[\\w|\\s]+$")
    private String text;

    @NotNull
    @Size(min = 3, max = 5)
    private List<Long> tags;

    public CreateFactDTO() {
    }

    public CreateFactDTO(String text, List<Long> tags) {
        this.text = text;
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }
}
