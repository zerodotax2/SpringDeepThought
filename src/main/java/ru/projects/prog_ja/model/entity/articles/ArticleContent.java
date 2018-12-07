package ru.projects.prog_ja.model.entity.articles;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ArticleContent")
public class ArticleContent {
    private long articleId;
    private String htmlContent;
    private String subtitle;
    private ArticleInfo article;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_content_id")
    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    @Column(name = "htmlContent")
    @Type(type = "org.hibernate.type.TextType")
    public String getHtmlContent() {
        return this.htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Basic
    @Column(name = "subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ArticleContent(){}

    public ArticleContent(String subtitle , String htmlContent) {
        this.htmlContent = htmlContent;
        this.subtitle = subtitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleContent that = (ArticleContent) o;
        return articleId == that.articleId &&
                Objects.equals(subtitle, that.subtitle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(articleId, subtitle);
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "article_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "article_content_fk"))
    public ArticleInfo getArticle() {
        return article;
    }

    public void setArticle(ArticleInfo article) {
        this.article = article;
    }
}
