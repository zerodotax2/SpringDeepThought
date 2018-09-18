package ru.projects.prog_ja.dto;

import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.Set;
import java.util.TreeSet;

public class TagsContainer extends DefaultDTO{

    protected Set<SmallTagTransfer> tags;

    public TagsContainer(long id){
        super(id);
        this.tags = new HashSet<>();
    }

    public Set<SmallTagTransfer> getTags() {
        return tags;
    }

    public void setTags(Set<SmallTagTransfer> tags) {
        this.tags = tags;
    }

}
