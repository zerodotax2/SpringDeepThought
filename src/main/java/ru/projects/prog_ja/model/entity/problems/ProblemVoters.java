package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "problem_voters")
public class ProblemVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_voters_id", nullable = false, unique = true)
    private long problemVotersId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false, foreignKey = @ForeignKey(name = "problem_voters_fk"))
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_problem_voters_fk"))
    private UserInfo user;

    public ProblemVoters() {
    }

    public ProblemVoters(Problem problem, UserInfo user) {
        this.problem = problem;
        this.user = user;
    }

    public long getProblemVotersId() {
        return problemVotersId;
    }

    public void setProblemVotersId(long problemVotersId) {
        this.problemVotersId = problemVotersId;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
