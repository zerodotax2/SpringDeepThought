package ru.projects.prog_ja.model.entity.articles;
import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "ArticlesTags")
public class ArticlesTags implements Comparable<ArticlesTags>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articles_tags_id", unique = true, nullable = false)
    private long articlesTagsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleInfo articleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_id", nullable = false)
    private Tags tagId;

    public ArticlesTags() {
    }

    public ArticlesTags(ArticleInfo articleId, Tags tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }

    public long getArticlesTagsId() {
        return articlesTagsId;
    }

    public void setArticlesTagsId(long articlesTagsId) {
        this.articlesTagsId = articlesTagsId;
    }

    public ArticleInfo getArticleId() {
        return articleId;
    }

    public void setArticleId(ArticleInfo articleId) {
        this.articleId = articleId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }

    @Override
    public int compareTo(ArticlesTags o) {
        return Long.compare(articlesTagsId, o.getArticlesTagsId());
    }
}
