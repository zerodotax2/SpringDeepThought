package ru.projects.prog_ja.dto.auth;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmailContainer {

    @NotNull
    @Size(min = 5, max = 50)
    @Pattern(regexp = "^[A-z|0-9|_|.]+?@[A-z]+?\\.[A-z]+?$")
    private String email;

    public EmailContainer() {
    }

    public EmailContainer(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
