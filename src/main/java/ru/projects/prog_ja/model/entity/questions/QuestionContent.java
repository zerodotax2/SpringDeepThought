package ru.projects.prog_ja.model.entity.questions;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.projects.prog_ja.model.entity.answer.Answer;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "QuestionContent")
public class QuestionContent {
    private long questionId;
    private String htmlContent;
    private Set<Answer> answers;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_content_id")
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Basic
    @Column(name = "htmlContent")
    public String getHtmlContent() {
        return this.htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public QuestionContent(){}

    public QuestionContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionContent that = (QuestionContent) o;
        return questionId == that.questionId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(questionId);
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "questionContent", orphanRemoval = true)
    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }


}
