package ru.projects.prog_ja.dto.full;

import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.commons.CommonArticleTransfer;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class FullArticleTransfer extends CommonArticleTransfer {

    private Set<CommonCommentTransfer> comments;
    private SmallUserTransfer user;
    private String subtitle;
    private String htmlContent;

    public FullArticleTransfer(long id, String title, String middleImage, Date createDate, int views, long rating,  SmallUserTransfer user, String subtitle, String htmlContent) {
        super(id, title, middleImage, createDate, views, rating);
        this.comments = new TreeSet<>();
        this.user = user;
        this.subtitle = subtitle;
        this.htmlContent = htmlContent;
    }

    public Set<CommonCommentTransfer> getComments() {
        return comments;
    }

    public void setComments(Set<CommonCommentTransfer> comments) {
        this.comments = comments;
    }

    public SmallUserTransfer getUser() {
        return user;
    }

    public void setUser(SmallUserTransfer user) {
        this.user = user;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
