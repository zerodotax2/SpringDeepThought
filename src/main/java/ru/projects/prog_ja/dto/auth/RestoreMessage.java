package ru.projects.prog_ja.dto.auth;

public class RestoreMessage {

    private final long userId;
    private final String token;

    public RestoreMessage(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
