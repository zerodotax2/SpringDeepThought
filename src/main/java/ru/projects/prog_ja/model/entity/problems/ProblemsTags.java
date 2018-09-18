package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "ProblemsTags")
public class ProblemsTags implements Comparable<ProblemsTags>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problems_tags_id", unique = true, nullable = false)
    private long problemsTagsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_id", nullable = false)
    private Tags tagId;

    public ProblemsTags() {
    }

    public ProblemsTags(Problem problemId, Tags tagId) {
        this.problemId = problemId;
        this.tagId = tagId;
    }

    public long getProblemsTagsId() {
        return problemsTagsId;
    }

    public void setProblemsTagsId(long questionTagsId) {
        this.problemsTagsId = questionTagsId;
    }

    public Problem getProblemId() {
        return problemId;
    }

    public void setProblemId(Problem problemId) {
        this.problemId = problemId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }

    @Override
    public int compareTo(ProblemsTags o) {
        return Long.compare(problemsTagsId, o.getProblemsTagsId());
    }
}
