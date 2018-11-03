package ru.projects.prog_ja.dto.view;


import java.util.List;

public class TagsContainerDTO {

    private List<Long> tags;

    public TagsContainerDTO() {
    }

    public TagsContainerDTO(List<Long> tags) {
        this.tags = tags;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }
}
