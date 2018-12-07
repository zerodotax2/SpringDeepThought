package ru.projects.prog_ja.model.entity.articles;

import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;

@Entity
@Table(name = "article_voters")
public class ArticleVoters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_voters_id", nullable = false, unique = true)
    private long articleVotersId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false, foreignKey = @ForeignKey(name = "article_voters_fk"))
    private ArticleInfo article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_article_voters_fk"))
    private UserInfo user;

    public ArticleVoters() {
    }

    public ArticleVoters(ArticleInfo article, UserInfo user) {
        this.article = article;
        this.user = user;
    }

    public long getArticleVotersId() {
        return articleVotersId;
    }

    public void setArticleVotersId(long articleVotersId) {
        this.articleVotersId = articleVotersId;
    }

    public ArticleInfo getArticle() {
        return article;
    }

    public void setArticle(ArticleInfo article) {
        this.article = article;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
