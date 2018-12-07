package ru.projects.prog_ja.model.entity.questions;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "question_voters")
public class QuestionVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_voters_id", unique = true, nullable = false)
    private long questionVotersId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "question_voters_fk"))
    private Questions question;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_question_voters_fk"))
    private UserInfo user;

    public QuestionVoters() {
    }

    public QuestionVoters(Questions question, UserInfo user) {
        this.question = question;
        this.user = user;
    }

    public long getQuestionVotersId() {
        return questionVotersId;
    }

    public void setQuestionVotersId(long questionVotersId) {
        this.questionVotersId = questionVotersId;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
