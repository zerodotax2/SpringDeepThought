package ru.projects.prog_ja.model.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "activate_tokens")
public class ActivateTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activate_token_id", unique = true, nullable = false)
    private long activateTokenId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "log_id", nullable = false, unique = true,foreignKey = @ForeignKey(name = "activate_tokens_fk"))
    private LogInfo logInfo;

    @Column(name = "token")
    private String token;

    public ActivateTokens() {
    }

    public ActivateTokens(LogInfo logInfo, String token) {
        this.logInfo = logInfo;
        this.token = token;
    }

    public long getActivateTokenId() {
        return activateTokenId;
    }

    public void setActivateTokenId(long activateTokenId) {
        this.activateTokenId = activateTokenId;
    }

    public LogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
