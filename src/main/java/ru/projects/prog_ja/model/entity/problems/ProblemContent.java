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

    @Column(name = "htmlContent", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String htmlContent;

    @OneToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", foreignKey = @ForeignKey(name = "problem_content_fk"),  unique = true, nullable = false)
    private Problem problem;


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

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
