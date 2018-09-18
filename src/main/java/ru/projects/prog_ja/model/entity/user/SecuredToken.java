package ru.projects.prog_ja.model.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "secured_token")
@org.hibernate.annotations.NamedQueries(
        @org.hibernate.annotations.NamedQuery(name = "getTokenByUserId", query = "select u.securedToken from UserInfo as u where u.userId = :id")
)
public class SecuredToken {

    public static final String GET_TOKEN_BY_USER = "getTokenByUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", unique = true, nullable = false)
    private long tokenId;

    @Column(name = "salt", nullable = false, length = 256)
    private String salt;

    @Column(name = "token", nullable = false, length = 256)
    private String token;

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
}
