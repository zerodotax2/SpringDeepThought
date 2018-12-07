package ru.projects.prog_ja.model.entity.articles;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "article_comment_voters")
public class ArticleCommentVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_voters_id", nullable = false, unique = true)
    private long articleCommentVotersId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_comment_id", nullable = false, foreignKey = @ForeignKey(name = "article_comment_voters_fk"))
    private ArticleComments comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_article_comment_voters_fk"))
    private UserInfo user;

    public ArticleCommentVoters() {
    }

    public ArticleCommentVoters(ArticleComments comment, UserInfo user) {
        this.comment = comment;
        this.user = user;
    }

    public long getArticleCommentVotersId() {
        return articleCommentVotersId;
    }

    public void setArticleCommentVotersId(long articleCommentVotersId) {
        this.articleCommentVotersId = articleCommentVotersId;
    }

    public ArticleComments getComment() {
        return comment;
    }

    public void setComment(ArticleComments comment) {
        this.comment = comment;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
