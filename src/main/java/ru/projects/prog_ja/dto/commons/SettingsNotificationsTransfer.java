package ru.projects.prog_ja.dto.commons;


import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.PagesDTO;

import java.util.Set;
import java.util.TreeSet;

public class SettingsNotificationsTransfer extends SmallUserTransfer {

    private String firstName;
    private String lastName;
    private Set<SmallNoticeTransfer> notifications;
    private long notices;
    private PagesDTO pages;

    public SettingsNotificationsTransfer(){}

    public SettingsNotificationsTransfer(long id, String login, String userImage, long rating, String firstName, String lastName, long notices) {
        super(id, login, userImage, rating);
        this.firstName = firstName;
        this.lastName = lastName;
        this.notifications = new TreeSet<>();
        this.pages = new PagesDTO(0,0,0,0);
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

    public Set<SmallNoticeTransfer> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<SmallNoticeTransfer> notifications) {
        this.notifications = notifications;
    }

    public long getNotices() {
        return notices;
    }

    public void setNotices(long notices) {
        this.notices = notices;
    }

    public PagesDTO getPages() {
        return pages;
    }

    public void setPages(PagesDTO pages) {
        this.pages = pages;
    }
}
