package ru.projects.prog_ja.model.entity.problems;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "ProblemsUsers")
public class ProblemsUsers implements Comparable<ProblemsUsers>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problems_users_id", nullable = false, unique = true)
    private long problemsUsersId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Column(name = "problem_id", nullable = false)
    private Problem problemId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userId;

    public ProblemsUsers(){}

    public ProblemsUsers(Problem problemId, UserInfo userId) {
        this.problemId = problemId;
        this.userId = userId;
    }

    public long getProblemsUsersId() {
        return problemsUsersId;
    }

    public void setProblemsUsersId(long problemsUsersId) {
        this.problemsUsersId = problemsUsersId;
    }

    public Problem getProblemId() {
        return problemId;
    }

    public void setProblemId(Problem problemId) {
        this.problemId = problemId;
    }

    public UserInfo getUserId() {
        return userId;
    }

    public void setUserId(UserInfo userId) {
        this.userId = userId;
    }

    @Override
    public int compareTo(ProblemsUsers o) {
        return Long.compare(problemsUsersId, o.getProblemsUsersId());
    }
}
