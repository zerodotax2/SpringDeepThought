package ru.projects.prog_ja.model.entity.questions;

import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "QuestionsTags")
public class QuestionsTags implements Comparable<QuestionsTags>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questions_tags_id", unique = true, nullable = false)
    private long questionTagsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "question_id", nullable = false)
    private Questions questionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_id", nullable = false)
    private Tags tagId;

    public QuestionsTags() {
    }

    public QuestionsTags(Questions questionId, Tags tagId) {
        this.questionId = questionId;
        this.tagId = tagId;
    }

    public long getQuestionTagsId() {
        return questionTagsId;
    }

    public void setQuestionTagsId(long questionTagsId) {
        this.questionTagsId = questionTagsId;
    }

    public Questions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Questions questionId) {
        this.questionId = questionId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }

    @Override
    public int compareTo(QuestionsTags o) {
        return Long.compare(questionTagsId, o.getQuestionTagsId());
    }
}
