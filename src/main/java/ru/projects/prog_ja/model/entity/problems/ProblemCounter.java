package ru.projects.prog_ja.model.entity.problems;

import javax.persistence.*;

@Entity
@Table(name = "problem_counter")
public class ProblemCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_counter_id", unique = true, nullable = false)
    private long problemCounterId;

    @Column(name = "attempts", nullable = false)
    private long attempts;

    @Column(name = "solved", nullable = false)
    private long solved;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "problem_counter_fk"))
    private Problem problem;

    public ProblemCounter() {
    }

    public ProblemCounter(long attempts, long solved) {
        this.attempts = attempts;
        this.solved = solved;
    }

    public long getProblemCounterId() {
        return problemCounterId;
    }

    public void setProblemCounterId(long problemCounterId) {
        this.problemCounterId = problemCounterId;
    }

    public long getAttempts() {
        return attempts;
    }

    public void setAttempts(long attempts) {
        this.attempts = attempts;
    }

    public long getSolved() {
        return solved;
    }

    public void setSolved(long solved) {
        this.solved = solved;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
