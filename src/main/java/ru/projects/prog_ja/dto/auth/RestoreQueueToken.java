package ru.projects.prog_ja.dto.auth;

import java.util.Date;

public class RestoreQueueToken {

    private final Date date;
    private final String token;

    public RestoreQueueToken(Date date, String token) {
        this.date = date;
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public String getToken() {
        return token;
    }
}
