package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

public class SettingsAccountTransfer extends SmallUserTransfer {

    private String firstName;
    private String lastName;
    private String email;

    public SettingsAccountTransfer(){}

    public SettingsAccountTransfer(long id, String login, String userImage, long rating,
                                   String firstName, String lastName, String email) {
        super(id, login, userImage, rating);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
