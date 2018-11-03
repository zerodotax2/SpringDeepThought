package ru.projects.prog_ja.dto;

import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class UserDTO {

    private long id;
    private String login;
    private long rating;
    private String userImage;
    private Role role;
    private Set<SmallTagTransfer> prefers;
    private Set<SmallNoticeTransfer> notices;

    public UserDTO(long id, String login, String userImage, long rating,  Role role) {
        this.id = id;
        this.login = login;
        this.userImage = userImage;
        this.role = role;
        this.rating = rating;
        this.prefers = new HashSet<>();
        this.notices = new TreeSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<SmallTagTransfer> getPrefers() {
        return prefers;
    }

    public void setPrefers(Set<SmallTagTransfer> prefers) {
        this.prefers = prefers;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public Set<SmallNoticeTransfer> getNotices() {
        return notices;
    }

    public void setNotices(Set<SmallNoticeTransfer> notices) {
        this.notices = notices;
    }
}
