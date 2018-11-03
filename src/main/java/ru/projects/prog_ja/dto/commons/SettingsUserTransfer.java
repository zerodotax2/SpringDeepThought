package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsUserTransfer extends SmallUserTransfer {

    private String firstName;
    private String lastName;
    private Date birthDate;
    private String about;
    private List<SmallTagTransfer> interests;

    public SettingsUserTransfer(){}

    public SettingsUserTransfer(long id, String login, String userImage, long rating,
                                String firstName, String lastName, String about, Date birthDate) {
        super(id, login, userImage, rating);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.about = about;
        this.interests = new ArrayList<>();
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<SmallTagTransfer> getInterests() {
        return interests;
    }

    public void setInterests(List<SmallTagTransfer> interests) {
        this.interests = interests;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
