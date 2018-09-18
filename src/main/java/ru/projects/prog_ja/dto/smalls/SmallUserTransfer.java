package ru.projects.prog_ja.dto.smalls;

public class SmallUserTransfer {

    protected long id;
    protected String login;
    protected String userImage;
    protected long rating;
    private boolean auth = false;

    public SmallUserTransfer(){}

    public SmallUserTransfer(long id, String login, String userImage, long rating) {
        this.id = id;
        this.login = login;
        this.userImage = userImage;
        this.rating = rating;
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

    public void setLogin(String firstName) {
        this.login = firstName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
