package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "secured_token")
@DynamicUpdate
public class SecuredToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", unique = true, nullable = false)
    private long tokenId;

    @Column(name = "salt", nullable = false, length = 256)
    private String salt;

    @Column(name = "token", nullable = false, length = 256)
    private String token;

    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "secured_token_fk"))
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private UserInfo user;

    public SecuredToken(){}

    public SecuredToken(String salt, String token) {
        this.salt = salt;
        this.token = token;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
