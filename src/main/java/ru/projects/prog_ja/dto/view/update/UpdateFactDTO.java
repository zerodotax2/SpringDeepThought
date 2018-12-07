package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateFactDTO {

    @NotNull
    private long factId;

    @NotNull
    @Size(min = 100, max = 1000)
    @Pattern(regexp = "^[А-я|ё|\\w|\\s|\\d|\\p{Punct}]+$")
    private String text;

    @NotNull
    @Size(min = 3, max = 5)
    private List<Long> tags;

    public UpdateFactDTO() {
    }

    public UpdateFactDTO(long factId, String text, List<Long> tags) {
        this.factId = factId;
        this.text = text;
        this.tags = tags;
    }

    public long getFactId() {
        return factId;
    }

    public void setFactId(long factId) {
        this.factId = factId;
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
