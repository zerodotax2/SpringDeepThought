package ru.projects.prog_ja.model.entity.questions;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "QuestionContent")
public class QuestionContent {

    private long questionId;
    private String htmlContent;
    private Questions question;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_content_id")
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Type(type = "org.hibernate.type.TextType")
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


    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "question_content_fk"))
    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }
}
