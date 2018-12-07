package ru.projects.prog_ja.dto.auth;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RestorePasswordContainer {

    @NotNull
    @Size(min = 8, max = 64)
    @Pattern(regexp = "^[A-z|А-я|0-9|_]+$")
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    @Size(min = 64, max = 64)
    private String token;

    public RestorePasswordContainer() {
    }

    public RestorePasswordContainer(String password, String confirmPassword, String token) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
