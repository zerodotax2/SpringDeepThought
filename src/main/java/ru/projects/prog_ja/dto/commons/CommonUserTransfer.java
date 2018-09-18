package ru.projects.prog_ja.dto.commons;

import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.Date;

public class CommonUserTransfer extends SmallUserTransfer {

    protected  String firstName;
    protected String lastName;
    protected Date createDate;
    protected Date birthDate;

    public CommonUserTransfer(long id, String login, String middleImage, long rating, String firstName, String lastName,
                              Date createDate, Date birthDate) {
        super(id, login, middleImage, rating);
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = createDate;
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
