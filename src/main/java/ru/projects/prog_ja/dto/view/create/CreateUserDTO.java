package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateUserDTO {

    @Size(min = 8, max = 50, message = "Длина email должна быть от 8 до 50 символов")
    @Pattern(regexp = "^[A-z|0-9|_|.]+@[A-z|.]+$", message = "Неверный формат email")
    @NotNull(message = "Поле не может быть пустым")
    private String email;

    @Size(min = 8, max = 64, message = "Длина пароля должна быть от 8 до 64 символов")
    @Pattern(regexp = "^[A-z|А-я|0-9|_]+$", message = "Пароль состоит только из следующих символов (A-z, А-я, 1-9, _)")
    @NotNull(message = "Поле не может быть пустым")
    private String password;
    private String confirmPassword;

    @Size(min = 3, max = 32, message = "Длина логина должна быть от 3 до 32 символов")
    @Pattern(regexp = "^[A-z|0-9|_]+$", message = "Логин состоит только из следующих символов (A-z, 1-9, _)")
    @NotNull(message = "Поле не может быть пустым")
    private String login;

    private boolean createCookie;

    public CreateUserDTO(){}

    public CreateUserDTO(String email, String password, String confirmPassword, boolean createCookie) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.createCookie = createCookie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isCreateCookie() {
        return createCookie;
    }

    public void setCreateCookie(boolean createCookie) {
        this.createCookie = createCookie;
    }
}
