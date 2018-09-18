package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "UserInbox")
@NamedQueries({
        @NamedQuery(name = "getAllUserNotices", query = "select new ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer (" +
                " ui.userInboxId, ui.header, ui.message, ui.noticeType, ui.createDate " +
                " ) from UserInbox ui " +
                " where ui.userInfo = :user "),
        @NamedQuery(name = "getLastUserNotices", query = "select new ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer (" +
                " ui.userInboxId, ui.header, ui.message, ui.noticeType, ui.createDate " +
                " ) from UserInbox ui " +
                " where ui.userInfo = :user and ui.active = true "),
        @NamedQuery(name = "unactivateNotice", query = "update UserInbox ui set ui.active = false where ui.userInboxId = :id ")
})
public class UserInbox {

    public static final String GET_ALL_USER_NOTICES = "getAllUserNotices";
    public static final String GET_LAST_USER_NOTICES = "getLastUserNotices";
    public static final String UNACTIVATE_NOTICE = "unactivateNotice";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_inbox_id", unique = true, nullable = false)
    private long userInboxId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "noticeType", nullable = false)
    private NoticeType noticeType;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "title", nullable = false, length = 255)
    private String header;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false,foreignKey = @ForeignKey(name = "inbox_user_fk"))
    private UserInfo userInfo;

    public UserInbox(){ }

    public UserInbox( String header, String message, NoticeType type) {
        this.noticeType = type;
        this.message = message;
        this.header = header;
        createDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInbox userInbox = (UserInbox) o;
        return userInboxId == userInbox.userInboxId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userInboxId);
    }

    public long getUserInboxId() {
        return userInboxId;
    }

    public void setUserInboxId(long userInboxId) {
        this.userInboxId = userInboxId;
    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
