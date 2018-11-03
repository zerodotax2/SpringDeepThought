package ru.projects.prog_ja.model.entity.problems;


import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ProblemContent")
public class ProblemContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_content_id", nullable = false, unique = true)
    private long problemContentId;

    @Type(type = "TextType")
    @Column(name = "htmlContent", nullable = false)
    private String htmlContent;


    public ProblemContent() {
    }

    public ProblemContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public long getProblemContentId() {
        return problemContentId;
    }

    public void setProblemContentId(long problemContentId) {
        this.problemContentId = problemContentId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
