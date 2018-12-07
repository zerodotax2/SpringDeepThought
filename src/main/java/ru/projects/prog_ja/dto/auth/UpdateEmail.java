package ru.projects.prog_ja.dto.auth;

public class UpdateEmail {

    private final long userId;
    private final String email;
    private final String token;

    public UpdateEmail(long userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
