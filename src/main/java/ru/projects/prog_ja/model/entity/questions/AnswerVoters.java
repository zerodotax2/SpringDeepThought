package ru.projects.prog_ja.model.entity.questions;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "answer_voters")
public class AnswerVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_voters_id", unique = true, nullable = false)
    private long answerVotersId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false, foreignKey = @ForeignKey(name = "answer_voters_fk"))
    private Answer answer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_answer_voters_fk"))
    private UserInfo user;

    public AnswerVoters() {
    }

    public AnswerVoters(Answer answer, UserInfo user) {
        this.answer = answer;
        this.user = user;
    }

    public long getAnswerVotersId() {
        return answerVotersId;
    }

    public void setAnswerVotersId(long answerVotersId) {
        this.answerVotersId = answerVotersId;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
