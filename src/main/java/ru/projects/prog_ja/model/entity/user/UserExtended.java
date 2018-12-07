package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "UserExtended")
public class UserExtended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_extended_id")
    private long user_extended_id;

    @Column(name = "firstName", length = 32)
    private String firstName;

    @Column(name = "lastName", length = 32)
    private String lastName;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "birthDate")
    private java.sql.Date birthDate;

    @Column(name = "bgImage")
    private String bgImage;

    @Column(name = "about")
    @Type(type = "org.hibernate.type.TextType")
    private String about;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_extended_fk"))
    private UserInfo user;

    public UserExtended(){}

    public UserExtended(String firstName, String lastName, java.sql.Date birthDate, String bgImage, String about) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = new Date();
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

    public java.sql.Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(java.sql.Date birthDate) {
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

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
