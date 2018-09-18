package ru.projects.prog_ja.view.dto;


import javax.validation.constraints.*;

public class LoginUserDTO {

    @Min(value = 5)
    @Max(value = 32)
    @Pattern(regexp = "^[A-z|1-9|_]+$")
    @NotNull
    private String login;

    @Min(value = 8)
    @Max(value = 64)
    @NotNull
    @Pattern(regexp = "^[A-z|1-9|А-я|_]+$")
    private String password;

    private boolean createCookie;

    public LoginUserDTO(){}

    public LoginUserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCreateCookie() {
        return createCookie;
    }

    public void setCreateCookie(boolean createCookie) {
        this.createCookie = createCookie;
    }
}
