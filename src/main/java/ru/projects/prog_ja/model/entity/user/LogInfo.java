package ru.projects.prog_ja.model.entity.user;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "logInfo")
public class LogInfo {

    private long logId;
    private String login;
    private String passH;
    private String email;
    private boolean enable = false;
    private UserRoles role;
    private UserInfo userInfo;
    private ActivateTokens token;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false, unique = true)
    public long getLogId() {
        return logId;
    }

    public void setLogId(long userId) {
        this.logId = userId;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        return logId == logInfo.logId &&
                Objects.equals(login, logInfo.login) &&
                Objects.equals(passH, logInfo.passH);
    }

    public LogInfo(){}

    public LogInfo(String login, String passH, String email, boolean enable) {
        this.login = login;
        this.passH = passH;
        this.email = email;
        this.enable = enable;
    }

    @Override
    public int hashCode() {

        return Objects.hash(logId, login, passH);
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


    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "log_info_fk"))
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "logInfo")
    public ActivateTokens getToken() {
        return token;
    }

    public void setToken(ActivateTokens token) {
        this.token = token;
    }
}
