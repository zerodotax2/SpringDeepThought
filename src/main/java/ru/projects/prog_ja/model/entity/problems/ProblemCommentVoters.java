package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "problem_comment_voters")
public class ProblemCommentVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_comment_voters_id", nullable = false, unique = true)
    private long problemCommentVotersId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_comment_id", nullable = false, foreignKey = @ForeignKey(name = "problem_comment_voters_fk"))
    private ProblemComment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_problem_comment_voters_fk"))
    private UserInfo user;

    public ProblemCommentVoters() {
    }

    public ProblemCommentVoters(ProblemComment comment, UserInfo user) {
        this.comment = comment;
        this.user = user;
    }

    public long getProblemCommentVotersId() {
        return problemCommentVotersId;
    }

    public void setProblemCommentVotersId(long problemCommentVotersId) {
        this.problemCommentVotersId = problemCommentVotersId;
    }

    public ProblemComment getComment() {
        return comment;
    }

    public void setComment(ProblemComment comment) {
        this.comment = comment;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
