package ru.projects.prog_ja.model.entity.support;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_questions")
@NamedQueries({
        @NamedQuery(name = "getUserForumQuestion", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u" +
                " where uq.userQuestionId = : id"),
        @NamedQuery(name = "getEntityUserQuestion", query = " select uq from UserQuestion uq" +
                " left join fetch uq.user u" +
                " where uq.userQuestionId = : id"),
        @NamedQuery(name = "getUserForumQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u" +
                " order by uq.createDate desc"),
        @NamedQuery(name = "getNonAnsweredUserQuestions", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserQuestionTransfer( " +
                " uq.userQuestionId, uq.text, uq.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from UserQuestion uq" +
                " left join uq.user u " +
                " where uq.answer is null " +
                " order by uq.createDate desc "),
        @NamedQuery(name = "getForumAnswer", query = "select new ru.projects.prog_ja.dto.smalls.SmallForumAnswer(" +
                " a.userForumAnswerId, a.text, a.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                ") from UserQuestion uq" +
                " left join uq.answer a " +
                " left join a.user u " +
                " where uq.answer is not null and uq.userQuestionId = :id"),
        @NamedQuery(name = "removeForumAnswer", query = "delete from UserForumAnswer where userForumAnswerId = :id and user = :user"),
        @NamedQuery(name = "removeForumQuestion", query = "delete from UserQuestion where userQuestionId = :id and user = :user"),
        @NamedQuery(name = "getUserQuestionNoticeTemplate", query = "select new ru.projects.prog_ja.dto.NoticeEntityTemplateDTO(" +
                " u.userId, uq.text " +
                " ) from UserQuestion uq left join uq.answer a left join a.user u where uq.userQuestionId = :id and uq.answer is not null")
})
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_question_id", nullable = false, unique = true)
    private long userQuestionId;

    @Column(name = "text")
    @Type(type = "org.hibernate.type.TextType")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_forum_question_fk"))
    private UserInfo user;

    @Column(name = "createDate")
    private Date createDate;

    @OneToOne(mappedBy = "forumQuestion", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserForumAnswer answer;

    public UserQuestion() {
    }

    public UserQuestion(String text, Date createDate) {
        this.text = text;
        this.createDate = createDate;
    }

    public long getUserQuestionId() {
        return userQuestionId;
    }

    public void setUserQuestionId(long userQuestionId) {
        this.userQuestionId = userQuestionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserForumAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(UserForumAnswer answer) {
        this.answer = answer;
    }
}
