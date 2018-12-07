package ru.projects.prog_ja.model.entity.support;

import org.hibernate.annotations.Type;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_forum_answers")
public class UserForumAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_forum_answer_id", nullable = false, unique = true)
    private long userForumAnswerId;

    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_answer_user_fk"))
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    private UserInfo user;

    @Column(name = "text", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String text;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_question_id", foreignKey = @ForeignKey(name = "user_forum_question_fk"), nullable = false, unique = true)
    private UserQuestion forumQuestion;

    public UserForumAnswer() {
    }

    public UserForumAnswer(String text, Date createDate) {
        this.text = text;
        this.createDate = createDate;
    }

    public long getUserForumAnswerId() {
        return userForumAnswerId;
    }

    public void setUserForumAnswerId(long userForumAnswerId) {
        this.userForumAnswerId = userForumAnswerId;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserQuestion getForumQuestion() {
        return forumQuestion;
    }

    public void setForumQuestion(UserQuestion forumQuestion) {
        this.forumQuestion = forumQuestion;
    }
}
