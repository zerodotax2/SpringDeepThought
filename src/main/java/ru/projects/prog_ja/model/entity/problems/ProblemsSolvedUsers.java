package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Table(name = "ProblemsSolvedUsers")
@Entity
public class ProblemsSolvedUsers implements Comparable<ProblemsSolvedUsers>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problems_solved_users_id", nullable = false, unique = true)
    private long problemsSolvedUsersId;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    public ProblemsSolvedUsers(){

    }

    public ProblemsSolvedUsers(Problem problem, UserInfo user) {
        this.problem = problem;
        this.user = user;
    }

    public long getProblemsSolvedUsersId() {
        return problemsSolvedUsersId;
    }

    public void setProblemsSolvedUsersId(long problemsSolvedUsersId) {
        this.problemsSolvedUsersId = problemsSolvedUsersId;
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

    @Override
    public int compareTo(ProblemsSolvedUsers o) {
        return this.problemsSolvedUsersId > o.problemsSolvedUsersId ? 1 : -1;
    }
}
