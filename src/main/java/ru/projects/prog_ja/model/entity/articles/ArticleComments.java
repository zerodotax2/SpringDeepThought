package ru.projects.prog_ja.model.entity.articles;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ArticleComments")
public class ArticleComments {
    private String comment;
    private long postCommentId;
    private UserInfo userInfo;
    private Date createDate;
    private ArticleInfo articleInfo;
    private long rating;



    public ArticleComments(){}

    public ArticleComments(String comment, UserInfo userInfo, ArticleInfo articleInfo) {
        this.comment = comment;
        this.userInfo = userInfo;
        this.articleInfo = articleInfo;
        this.createDate = new Date();
        rating = 0;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id")
    public long getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(long postCommentId) {
        this.postCommentId = postCommentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComments that = (ArticleComments) o;
        return postCommentId == that.postCommentId &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(comment, postCommentId);
    }

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Column(nullable = false, name = "createDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    public ArticleInfo getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(ArticleInfo articleInfo) {
        this.articleInfo = articleInfo;
    }

    @Column(name = "rating")
    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
