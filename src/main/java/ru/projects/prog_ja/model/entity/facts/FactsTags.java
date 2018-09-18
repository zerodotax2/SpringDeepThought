package ru.projects.prog_ja.model.entity.facts;

import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "FactsTags")
public class FactsTags implements Comparable<FactsTags>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facts_tags_id", unique = true, nullable = false)
    private long factsTagsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fact_id", nullable = false)
    private Facts factId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_id", nullable = false)
    private Tags tagId;

    public FactsTags() {
    }

    public FactsTags(Facts factId, Tags tagId) {
        this.factId = factId;
        this.tagId = tagId;
    }

    public long getFactsTagsId() {
        return factsTagsId;
    }

    public void setFactsTagsId(long factsTagsId) {
        this.factsTagsId = factsTagsId;
    }

    public Facts getFactId() {
        return factId;
    }

    public void setFactId(Facts factId) {
        this.factId = factId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }

    @Override
    public int compareTo(FactsTags o) {
        return Long.compare(factsTagsId, o.getFactsTagsId());
    }
}
