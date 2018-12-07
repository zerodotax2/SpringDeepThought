package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ProblemSolution")
public class ProblemSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_solution_id")
    private long problemSolutionId;

    @Column(name = "solution", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String solution;

    @Column(name = "answer", nullable = false, length = 256)
    private String answer;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "problem_id", nullable = false, unique = true,foreignKey = @ForeignKey(name = "problem_solution_fk"))
    private Problem problem;


    public ProblemSolution(){}

    public ProblemSolution(String solution, String answer) {
        this.solution = solution;
        this.answer = answer;
    }

    public long getProblemSolutionId() {
        return problemSolutionId;
    }

    public void setProblemSolutionId(long problemSolutionId) {
        this.problemSolutionId = problemSolutionId;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
