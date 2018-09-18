package ru.projects.prog_ja.model.entity.user;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "logInfo")
@org.hibernate.annotations.NamedQueries(
        @org.hibernate.annotations.NamedQuery(name = "updatePassword", query = "update LogInfo set passH = :pass where userId in (select logId from UserInfo where userId = :id) ")
)
public class LogInfo {

    public final static String UPDATE_PASSWORD = "updatePassword";

    private long userId;
    private String logH;
    private String passH;
    private boolean enable = false;
    private UserRoles role;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "login_h")
    public String getLogH() {
        return this.logH;
    }

    public void setLogH(String logH) {
        this.logH = logH;
    }

    @Basic
    @Column(name = "pass_h")
    public String getPassH() {
        return passH;
    }

    public void setPassH(String passH) {
        this.passH = passH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInfo logInfo = (LogInfo) o;
        return userId == logInfo.userId &&
                Objects.equals(logH, logInfo.logH) &&
                Objects.equals(passH, logInfo.passH);
    }

    public LogInfo(){}

    public LogInfo(String logH, String passH, boolean enable) {
        this.logH = logH;
        this.passH = passH;
        this.enable = enable;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, logH, passH);
    }


    @Basic
    @Column(name = "enable")
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @OneToOne(mappedBy = "logInfo", cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
