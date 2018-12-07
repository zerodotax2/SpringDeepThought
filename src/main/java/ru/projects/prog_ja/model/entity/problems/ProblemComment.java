package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.Type;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ProblemComment")
public class ProblemComment implements Comparable<ProblemComment>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_comment_id")
    private long problemCommentId;

 
    @Column(name = "comment", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String comment;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Column(name = "rating", nullable = false)
    private long rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_problem_comment_fk"))
    private UserInfo creator;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false, foreignKey = @ForeignKey(name = "problem_comments_fk"))
    private Problem problem;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comment")
    private List<ProblemCommentVoters> voters;


    public ProblemComment(){}

    public ProblemComment(String comment) {
        this.comment = comment;
        this.createDate = new Date();
        this.rating = 0;
    }

    @Override
    public int compareTo(ProblemComment o) {
        return createDate.after(o.createDate) ? 1 : -1;
    }

    public long getProblemCommentId() {
        return problemCommentId;
    }

    public void setProblemCommentId(long problemCommentId) {
        this.problemCommentId = problemCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public UserInfo getCreator() {
        return creator;
    }

    public void setCreator(UserInfo creator) {
        this.creator = creator;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public List<ProblemCommentVoters> getVoters() {
        return voters;
    }

    public void setVoters(List<ProblemCommentVoters> voters) {
        this.voters = voters;
    }
}
