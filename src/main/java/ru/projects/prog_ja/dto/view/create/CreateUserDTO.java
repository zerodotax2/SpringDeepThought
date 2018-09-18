package ru.projects.prog_ja.dto.view.create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreateUserDTO {

    @Min(value = 8, message = "Минимальная длина email - 8 символов")
    @Max(value = 50, message = "Максимальная длина email - 50 символов")
    @Pattern(regexp = "^[A-z|1-9|_]+@[A-z|\\.]+$", message = "Неверный формат email")
    @NotNull(message = "Поле не может быть пустым")
    private String email;

    @Min(value = 8, message = "Минимальная длина пароля - 8 символов")
    @Max(value = 64, message = "Максимальная длина пароля - 64 символа")
    @Pattern(regexp = "^[A-z|А-я|1-9|_]+$", message = "Пароль состоит только из следующих символов (A-z, А-я, 1-9, _)")
    @NotNull(message = "Поле не может быть пустым")
    private String password;
    private String confirmPassword;

    @Min(value = 3, message = "Минимальная длина логина - 3 символа")
    @Max(value = 32, message = "Максимальная длина логина - 32 символа")
    @Pattern(regexp = "^[A-z|1-9|_]+$", message = "Логин состоит только из следующих символов (A-z, 1-9, _)")
    @NotNull(message = "Поле не может быть пустым")
    private String login;

    private boolean createCookie;

    public CreateUserDTO(){}

    public CreateUserDTO(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public boolean isCreateCookie() {
        return createCookie;
    }

    public void setCreateCookie(boolean createCookie) {
        this.createCookie = createCookie;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
