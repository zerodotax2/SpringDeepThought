package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.TagsContainer;

import java.util.Set;

public class CommonFactTransfer extends TagsContainer {

    private String text;

    public CommonFactTransfer(long id, String text) {
        super(id);
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

    public Set<SmallTagTransfer> getTags() {
        return tags;
    }

    public void setTags(Set<SmallTagTransfer> tags) {
        this.tags = tags;
    }
}
