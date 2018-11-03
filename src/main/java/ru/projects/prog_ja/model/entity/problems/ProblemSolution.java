package ru.projects.prog_ja.model.entity.problems;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "ProblemSolution")
public class ProblemSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_solution_id")
    private long problemSolutionId;

    @Type(type = "TextType")
    @Column(name = "solution", nullable = false)
    private String solution;

    @Column(name = "answer", nullable = false, length = 256)
    private String answer;


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
}
