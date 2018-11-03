package ru.projects.prog_ja.model.entity.articles;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ArticleInfo")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getSmallArticles", query = "select new ru.projects.prog_ja.dto.smalls.SmallArticleTransfer(p.articleId, p.title, p.smallImagePath, p.rating)" +
                " from ArticleInfo p " +
                " order by p.views desc "),
        @org.hibernate.annotations.NamedQuery(name = "getCommonArticles", query = "select new ru.projects.prog_ja.dto.commons.CommonArticleTransfer (" +
                " p.articleId, p.title, p.middleImagePath, p.createDate, p.views, p.rating" +
                " ) " +
                " from ArticleInfo p " +
                " group by p.articleId  " +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "getFullArticle", query = "select p from ArticleInfo p " +
                " left join fetch p.articleContent as content" +
                " left join fetch p.userInfo as u " +
                " left join fetch p.tags as t" +
                " left join fetch t.tagId " +
                " left join fetch p.articleComments pcs" +
                " where p.articleId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "findSmallArticles", query = "select new ru.projects.prog_ja.dto.smalls.SmallArticleTransfer(p.articleId, p.title, p.smallImagePath, p.rating)" +
                " from ArticleInfo p" +
                " left join p.articleContent pc " +
                " where lower(p.title) like lower(:search)" +
                " or lower(pc.subtitle) like lower(:search) " +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "findCommonArticles", query = "select new ru.projects.prog_ja.dto.commons.CommonArticleTransfer(" +
                " p.articleId, p.title, p.middleImagePath, p.createDate, p.views, p.rating" +
                " ) " +
                " from ArticleInfo p " +
                " left join p.articleContent pc  " +
                " where lower(p.title) like lower(:search)" +
                " or lower(pc.subtitle) like lower(:search) " +
                " group by p.articleId  " +
                " order by p.createDate desc"),
        @org.hibernate.annotations.NamedQuery(name = "deleteArticle", query = "delete from ArticleInfo p where p.articleId = :id and p.userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "updateArticleComment", query = "update ArticleComments set comment = :comment where postCommentId = :id and userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "updateArticleCommentRate", query = "update ArticleComments set rating = rating + :rate where postCommentId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "deleteArticleComment", query = "delete from ArticleComments where postCommentId = :id and userInfo = :user"),
        @org.hibernate.annotations.NamedQuery(name = "getArticleComment", query = "select new ru.projects.prog_ja.dto.CommonCommentTransfer(" +
                " ac.postCommentId, ac.comment, ac.rating, ac.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                ") from ArticleComments ac " +
                " left join ac.userInfo as u " +
                " where ac.postCommentId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "countArticles", query = "select distinct count(a.articleId) from Articles a"),
        @org.hibernate.annotations.NamedQuery(name = "changeArticleRate", query = "update Articles set rating = rating + :rate where articleId = :id")
})
public class ArticleInfo {

    public static final String GET_SMALL_ARTICLES = "getSmallArticles";
    public static final String GET_COMMON_ARTICLES = "getCommonArticles";
    public static final String GET_FULL_ARTICLE = "getFullArticle";
    public static final String DELETE_ARTICLE = "deleteArticle";
    public static final String FIND_SMALL_ARTICLES = "findSmallArticles";
    public static final String FIND_COMMON_ARTICLES = "findCommonArticles";
    public static final String UPDATE_ARTICLE_COMMENT = "updateArticleComment";
    public static final String UPDATE_ARTICLE_COMMENT_RATE = "updateArticleCommentRate";
    public static final String DELETE_ARTICLE_COMMENT = "deleteArticleComment";
    public static final String GET_ARTICLE_COMMENT = "getArticleComment";
    public static final String COUNT_ARTICLES = "countArticles";
    public static final String CHANGE_ARTICLE_RATE = "changeArticleRate";

    private long articleId;
    private String title;
    private Date createDate;
    private Integer views;
    private long rating;
    private Set<ArticlesTags> tags;
    private ArticleContent articleContent;
    private UserInfo userInfo;
    private Set<ArticleComments> articleComments;
    private boolean activated = true;

    private String smallImagePath;
    private String middleImagePath;
    private String largeImagePath;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", unique = true, nullable = false)
    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    @Basic
    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "createDate", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "views")
    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public ArticleInfo(){}

    public ArticleInfo(String title, String smallImagePath, String middleImagePath, String largeImagePath) {
        this.title = title;
        this.createDate = new Date(new java.util.Date().getTime());
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
        rating = 0;
        views = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleInfo articleInfo = (ArticleInfo) o;
        return articleId == articleInfo.articleId && title.equals(articleInfo.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(articleId, title);
    }

    @Override
    public String toString(){

        StringBuilder builder = new StringBuilder("Имя: " + this.title + "\n Теги: \n");

        for(ArticlesTags tag : tags){
            builder.append( tag.getTagId().getName() );
        }

        return builder.toString();
    }

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "articleId")
    public Set<ArticlesTags> getTags() {
        return tags;
    }

    public void setTags(Set<ArticlesTags> tags) {
        this.tags = tags;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    @JoinColumn(name = "article_content_id", nullable = false, unique = true)
    public ArticleContent getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(ArticleContent articleContent) {
        this.articleContent = articleContent;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articleInfo", orphanRemoval = true)
    public Set<ArticleComments> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(Set<ArticleComments> articleComments) {
        this.articleComments = articleComments;
    }

    @Basic
    @Column(name = "activated", nullable = false)
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Column(name = "smallImagePath", nullable = false, unique = true, length = 300)
    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    @Column(name = "middleImagePath", nullable = false, unique = true, length = 300)
    public String getMiddleImagePath() {
        return middleImagePath;
    }

    public void setMiddleImagePath(String middleImagePath) {
        this.middleImagePath = middleImagePath;
    }

    @Column(name = "largeImagePath", nullable = false, unique = true, length = 300)
    public String getLargeImagePath() {
        return largeImagePath;
    }

    public void setLargeImagePath(String largeImagePath) {
        this.largeImagePath = largeImagePath;
    }

    @Column(name = "rating")
    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
