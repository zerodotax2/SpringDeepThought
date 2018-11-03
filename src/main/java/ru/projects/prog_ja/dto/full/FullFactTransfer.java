package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;

public class FullFactTransfer extends CommonFactTransfer {

    private SmallUserTransfer user;

    public FullFactTransfer(long id, String text, SmallUserTransfer user) {
        super(id, text);
        this.user = user;
    }

    public FullFactTransfer(long id, String text, long userId, String login, String userImage, long rating) {
        super(id, text);
        this.user = new SmallUserTransfer(userId, login, userImage, rating);
    }


    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }
}
