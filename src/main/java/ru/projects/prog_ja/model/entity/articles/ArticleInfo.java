package ru.projects.prog_ja.model.entity.articles;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ArticleInfo")
@NamedQueries({
        @NamedQuery(name = "getSmallArticles", query = "select new ru.projects.prog_ja.dto.smalls.SmallArticleTransfer(p.articleId, p.title, p.smallImagePath, p.rating)" +
                " from ArticleInfo p " +
                " order by p.views desc "),
        @NamedQuery(name = "getCommonArticles", query = "select new ru.projects.prog_ja.dto.commons.CommonArticleTransfer (" +
                " p.articleId, p.title, p.middleImagePath, p.createDate, p.views, p.rating" +
                " ) " +
                " from ArticleInfo p " +
                " group by p.articleId  " +
                " order by p.createDate desc"),
        @NamedQuery(name = "getFullArticle", query = "select new ru.projects.prog_ja.dto.full.FullArticleTransfer(" +
                " a.articleId, a.title,  a.largeImagePath, a.createDate, a.views, a.rating, u.userId, u.login, u.smallImagePath, u.rating, c.subtitle, c.htmlContent, a.smallImagePath, a.middleImagePath  " +
                " ) from ArticleInfo a" +
                " left join a.articleContent c" +
                " left join a.userInfo u " +
                " where a.articleId = :id"),
        @NamedQuery(name = "findSmallArticles", query = "select new ru.projects.prog_ja.dto.smalls.SmallArticleTransfer(p.articleId, p.title, p.smallImagePath, p.rating)" +
                " from ArticleInfo p" +
                " left join p.articleContent pc " +
                " where lower(p.title) like lower(:search)" +
                " or lower(pc.subtitle) like lower(:search) " +
                " order by p.createDate desc"),
        @NamedQuery(name = "findCommonArticles", query = "select new ru.projects.prog_ja.dto.commons.CommonArticleTransfer(" +
                " p.articleId, p.title, p.middleImagePath, p.createDate, p.views, p.rating" +
                " ) " +
                " from ArticleInfo p " +
                " left join p.articleContent pc  " +
                " where lower(p.title) like lower(:search)" +
                " or lower(pc.subtitle) like lower(:search) " +
                " group by p.articleId  " +
                " order by p.createDate desc"),
        @NamedQuery(name = "deleteArticle", query = "delete from ArticleInfo p where p.articleId = :id and p.userInfo = :user"),
        @NamedQuery(name = "updateArticleComment", query = "update ArticleComments set comment = :comment where postCommentId = :id and userInfo = :user"),
        @NamedQuery(name = "updateArticleCommentRate", query = "update ArticleComments set rating = rating + :rate where postCommentId = :id and userInfo != :user"),
        @NamedQuery(name = "deleteArticleComment", query = "delete from ArticleComments where postCommentId = :id and userInfo = :user"),
        @NamedQuery(name = "getArticleComment", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer(" +
                " ac.postCommentId, ac.comment, ac.rating, ac.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                ") from ArticleComments ac " +
                " left join ac.userInfo as u " +
                " where ac.postCommentId = :id"),
        @NamedQuery(name = "countArticles", query = "select distinct count(a.articleId) from ArticleInfo a"),
        @NamedQuery(name = "changeArticleRate", query = "update ArticleInfo set rating = rating + :rate where articleId = :id and userInfo != :user"),
        @NamedQuery(name = "getArticleTitle", query = "select a.title from ArticleInfo a where a.articleId = :id "),
        @NamedQuery(name = "updateArticleOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :article in elements(userArticles) and userId != :user"),
        @NamedQuery(name = "updateArticleCommentOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :comment in elements(userArticleComments) and userId != :user"),
        @NamedQuery(name = "updateArticleView", query = "update ArticleInfo set views = views + :view where articleId = :id "),
        @NamedQuery(name = "getArticleNoticeTemplate", query = "select new ru.projects.prog_ja.dto.NoticeEntityTemplateDTO (" +
                " u.userId, a.title " +
                " ) from ArticleInfo a left join a.userInfo u where a.articleId = :id"),
        @NamedQuery(name = "getArticleNoticeCommentTemplate", query = "select new ru.projects.prog_ja.dto.NoticeCommentTemplateDTO (" +
                " a.articleId, u.userId, a.title " +
                " ) from ArticleComments ac left join ac.articleInfo a left join ac.userInfo u where ac.postCommentId = :id"),
        @NamedQuery(name = "getTagsByArticle", query = "select t.tagId from ArticleInfo a left join a.tags at left join at.tagId t where a.articleId = :id"),
        @NamedQuery(name = "getArticleComments", query = "select new ru.projects.prog_ja.dto.commons.CommonCommentTransfer( " +
                " ac.postCommentId, ac.comment, ac.rating, ac.createDate, u.userId, u.login, u.smallImagePath, u.rating " +
                " ) from ArticleComments ac " +
                " left join ac.userInfo u " +
                " where ac.articleInfo = :article"),
        @NamedQuery(name = "getSmallArticleTags",query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer(" +
                " t.tagId, t.name, t.color " +
                " ) from Tags t left join t.articles ta where ta.articleId = :article"),
        @NamedQuery(name = "getUpdateEntityArticle", query = "select a from ArticleInfo a " +
                " left join fetch a.tags t " +
                " left join fetch t.tagId ti" +
                " left join fetch ti.tagCounter tc " +
                " left join fetch a.articleContent c" +
                " where a.articleId = :id"),
        @NamedQuery(name = "removeArticleTags", query = "delete from ArticlesTags where tagId in (:tags)"),
        @NamedQuery(name = "isArticleVoted", query = "select articleVotersId from ArticleVoters where article = :article and user = :user"),
        @NamedQuery(name = "isCommentArticleVoted", query = "select articleCommentVotersId from ArticleCommentVoters where comment = :comment and user = :user")
})
public class ArticleInfo {

    private long articleId;
    private String title;
    private Date createDate;
    private long views;
    private long rating;
    private Set<ArticlesTags> tags;
    private List<ArticleVoters> voters;
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
    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public ArticleInfo(){}

    public ArticleInfo(String title, String smallImagePath, String middleImagePath, String largeImagePath) {
        this.title = title;
        this.createDate = new Date();
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "articleId", cascade = CascadeType.ALL)
    public Set<ArticlesTags> getTags() {
        return tags;
    }

    public void setTags(Set<ArticlesTags> tags) {
        this.tags = tags;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "article", fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
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

    @Column(name = "smallImagePath", nullable = false, length = 300)
    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    @Column(name = "middleImagePath", nullable = false, length = 300)
    public String getMiddleImagePath() {
        return middleImagePath;
    }

    public void setMiddleImagePath(String middleImagePath) {
        this.middleImagePath = middleImagePath;
    }

    @Column(name = "largeImagePath", nullable = false, length = 300)
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

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "article")
    public List<ArticleVoters> getVoters() {
        return voters;
    }

    public void setVoters(List<ArticleVoters> voters) {
        this.voters = voters;
    }
}
