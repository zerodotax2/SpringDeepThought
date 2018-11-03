package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "problem_feedback")
public class ProblemFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_feedback_id", unique = true, nullable = false)
    private long problemFeedbackId;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_problem_feedback_fk"))
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private UserInfo userInfo;

    @JoinColumn(name = "problem_id", nullable = false, foreignKey = @ForeignKey(name = "problem_feedback_fk"))
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Problem problem;

    public ProblemFeedback(String text, Date createDate) {
        this.text = text;
        this.createDate = createDate;
    }

    public long getProblemFeedbackId() {
        return problemFeedbackId;
    }

    public void setProblemFeedbackId(long problemFeedbackId) {
        this.problemFeedbackId = problemFeedbackId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
