package ru.projects.prog_ja.model.entity.user;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "UserExtended")
public class UserExtended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_extended_id")
    private long user_extended_id;

    @Basic
    @Column(name = "email", length = 50)
    private String email;

    @Basic
    @Column(name = "firstName", length = 32)
    private String firstName;

    @Basic
    @Column(name = "lastName", length = 32)
    private String lastName;

    @Basic
    @Column(name = "createDate")
    private Date createDate;

    @Basic
    @Column(name = "birthDate")
    private Date birthDate;

    @Basic
    @Column(name = "bgImage")
    private String bgImage;

    @Basic
    @Column(name = "about")
    private String about;

    public UserExtended(){}

    public UserExtended(String email, String firstName, String lastName, Date birthDate, String bgImage, String about) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = new Date(new java.util.Date().getTime());
        this.birthDate = birthDate;
        this.bgImage = bgImage;
        this.about = about;
    }

    public long getUser_extended_id() {
        return user_extended_id;
    }

    public void setUser_extended_id(long user_extended_id) {
        this.user_extended_id = user_extended_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
