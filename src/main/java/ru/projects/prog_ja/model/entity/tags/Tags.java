package ru.projects.prog_ja.model.entity.tags;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.articles.ArticlesTags;
import ru.projects.prog_ja.model.entity.facts.FactsTags;
import ru.projects.prog_ja.model.entity.problems.ProblemsTags;
import ru.projects.prog_ja.model.entity.questions.QuestionsTags;
import ru.projects.prog_ja.model.entity.user.UserInfo;
import ru.projects.prog_ja.model.entity.user.UsersTags;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Tags")
@NamedQueries({
        @NamedQuery(name = "getSmallTag", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer (" +
                " t.tagId, t.name, t.color " +
                " ) " +
                " from Tags t where t.tagId = :id"),
        @NamedQuery(name = "getFullTag", query = "select new ru.projects.prog_ja.dto.full.FullTagTransfer( " +
                " t.tagId, t.name, t.description, t.color, tc.articles, tc.questions, tc.problems, tc.users, tc.facts " +
                " ) from Tags t " +
                " left join t.tagCounter as tc " +
                " where t.tagId = :id"),
        @NamedQuery(name = "deleteTag", query = "delete from Tags t where t.tagId = :id"),
        @NamedQuery(name = "getSmallPopularTags", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer ( " +
                " t.id, t.name, t.color " +
                " ) " +
                " from Tags t" +
                " left join t.tagCounter as tc" +
                " order by tc.articles + tc.questions + tc.problems + tc.users + tc.facts "),
        @NamedQuery(name = "getCommonPopularTags", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer ( " +
                " t.id, t.name, t.color, t.description, tc.articles, tc.questions, tc.problems " +
                " ) " +
                " from Tags t" +
                " left join t.tagCounter as tc" +
                " order by tc.articles + tc.questions + tc.problems + tc.users + tc.facts "),
        @NamedQuery(name = "getCommonTags", query = "select new ru.projects.prog_ja.dto.commons.CommonTagTransfer(" +
                " t.tagId, t.name, t.color, t.description, tc.articles, tc.questions, tc.problems  " +
                "  ) from Tags t" +
                " left join t.tagCounter as tc  "),
        @NamedQuery(name = "getCommonTag", query = "select new ru.projects.prog_ja.dto.commons.CommonTagTransfer(" +
                " t.tagId, t.name, t.color, t.description, tc.articles, tc.questions, tc.problems  " +
                "  ) from Tags t" +
                " left join t.tagCounter as tc  " +
                " where t.tagId = :id"),
        @NamedQuery(name = "findSmallTags", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer (" +
                "  t.tagId, t.name, t.color " +
                " )  " +
                " from Tags t " +
                " where lower(t.name) like lower(:search)" +
                " or lower(t.description) like lower(:search) "),
        @NamedQuery(name = "countTags", query = "select distinct count(t.tagId) from Tags t")
})
public class Tags {

    public static final String DELETE_TAG = "deleteTag";
    public static final String GET_SMALL_TAG = "getSmallTag";
    public static final String GET_FULL_TAG = "getFullTag";
    public static final String GET_COMMON_TAGS = "getCommonTags";
    public static final String GET_COMMON_TAG = "getCommonTag";
    public static final String GET_SMALL_POPULAR_TAGS = "getSmallPopularTags";
    public static final String GET_COMMON_POPULAR_TAGS = "getCommonPopularTags";
    public static final String FIND_SMALL_TAGS = "findSmallTags";
    public static final String COUNT_TAGS = "countTags";

    private long tagId;
    private String name;
    private String description;
    private String color;

    private UserInfo creator;
    private Date createDate;

    private Set<FactsTags> facts;
    private Set<ArticlesTags> articles;
    private Set<QuestionsTags> questions;
    private Set<UsersTags> users;
    private Set<ProblemsTags> problems;
    private TagCounter tagCounter;

    private boolean activated = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", unique = true, nullable = false)
    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public Tags(){}

    public Tags(String name, String description, String color, UserInfo creator){
        this.name = name;
        this.description = description;
        this.color = color;
        this.creator = creator;
        this.tagCounter = new TagCounter();
        this.createDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tags = (Tags) o;
        return tagId == tags.tagId &&
                Objects.equals(name, tags.name) &&
                Objects.equals(description, tags.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tagId, name, description);
    }

    @Override
    public String toString(){
        return "Name: " + this.getName() + "\n" +
                "Description: " + this.getDescription();
    }

    @OneToMany(mappedBy = "tagId", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<FactsTags> getFacts() {
        return facts;
    }

    public void setFacts(Set<FactsTags> facts) {
        this.facts = facts;
    }

    @OneToMany(mappedBy = "tagId", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<ArticlesTags> getArticles() {
        return articles;
    }

    public void setArticles(Set<ArticlesTags> articles) {
        this.articles = articles;
    }

    @OneToMany(mappedBy = "tagId", fetch = FetchType.LAZY )
    @BatchSize(size = 10)
    public Set<QuestionsTags> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionsTags> questions) {
        this.questions = questions;
    }

    @Basic
    @Column(name = "activated", nullable = false)
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Column(name = "color", length = 7, nullable = false)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @OneToMany(mappedBy = "tagId", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<UsersTags> getUsers() {
        return users;
    }

    public void setUsers(Set<UsersTags> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "tagId", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<ProblemsTags> getProblems() {
        return problems;
    }

    public void setProblems(Set<ProblemsTags> problems) {
        this.problems = problems;
    }

    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_counter_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "tag_counter_fk"))
    public TagCounter getTagCounter() {
        return tagCounter;
    }

    public void setTagCounter(TagCounter tagCounter) {
        this.tagCounter = tagCounter;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name="tag_user_creator_fk"))
    public UserInfo getCreator() {
        return creator;
    }

    public void setCreator(UserInfo creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
