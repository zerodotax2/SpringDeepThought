package ru.projects.prog_ja.dto.auth;

public class AuthDTO {

    private long userId;
    private String login;
    private String email;
    private boolean active;

    public AuthDTO(long userId, String login, String email, boolean active) {
        this.userId = userId;
        this.login = login;
        this.email = email;
        this.active = active;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
