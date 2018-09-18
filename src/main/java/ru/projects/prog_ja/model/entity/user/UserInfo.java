package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.answer.Answer;
import ru.projects.prog_ja.model.entity.articles.ArticleComments;
import ru.projects.prog_ja.model.entity.articles.ArticleInfo;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.problems.ProblemsUsers;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "UserInfo")
@DynamicUpdate
@NamedQueries({
        @NamedQuery(name = "getSmallUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " where u.userId = :id"),
        @NamedQuery(name = "getSmallUsers", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " order by u.rating desc "),
        @NamedQuery(name = "getSmallUserByLogin", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " left join u.logInfo l" +
                " where l.login = :login"),
        @NamedQuery(name = "getCommonUser", query = "select new ru.projects.prog_ja.dto.commons.CommonUserTransfer(" +
                " u.userId, u.login, u.middleImagePath, u.rating, ue.firstName, ue.lastName, ue.createDate, ue.birthDate" +
                " ) " +
                " from UserInfo u " +
                " left join u.userExtended as ue " +
                " where u.userId = :id"),
        @NamedQuery(name = "deleteUser", query = "delete from UserInfo u where u.userId = :id"),
        @NamedQuery(name = "findSmallUsers", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " join u.userExtended as ue " +
                " where lower( ue.firstName ) like lower( :search )" +
                " or lower( ue.lastName ) like lower( :search ) " +
                " or lower(u.login) like lower(:search) "),
        @NamedQuery(name = "findCommonUsers", query = "select new ru.projects.prog_ja.dto.commons.CommonUserTransfer(" +
                " u.userId, u.login, u.middleImagePath, u.rating, ue.firstName, ue.lastName, ue.createDate, ue.birthDate" +
                " )" +
                " from UserInfo u " +
                " left join u.userExtended as ue" +
                " where lower(ue.firstName) like lower(:search) " +
                " or lower (ue.lastName) like lower(:search) " +
                " or lower (u.login) like lower (:search) "),
        @NamedQuery(name = "getUsersByBirthDate", query = "select new ru.projects.prog_ja.dto.commons.CommonUserTransfer(" +
                " u.userId, u.login, u.middleImagePath, u.rating, ue.firstName, ue.lastName, ue.createDate, ue.birthDate" +
                " ) " +
                " from UserInfo u " +
                " left join u.userExtended as ue" +
                " where ue.birthDate = :date"),
        @NamedQuery(name = "getUsersEmailsByDate", query = "select ue.email from UserExtended ue where ue.birthDate = :date"),
        @NamedQuery(name = "getFullUser", query = "select u " +
                " from UserInfo u " +
                " left outer join fetch u.userCounter " +
                " left outer join fetch u.userExtended " +
                " left outer join fetch u.interests as i" +
                " left outer join fetch i.tagId " +
                " where u.userId = :id"),
        @NamedQuery(name = "checkUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer " +
                " ( u.userId, u.login, u.smallImagePath, u.rating ) " +
                " from UserInfo u " +
                " left join u.logInfo as l " +
                " where l.logH = :login and l.passH = :pass"),
        @NamedQuery(name = "updateImage", query = "update UserInfo set smallImagePath = :small, set middleImagePath = :middle, set largeImagePath = :large where userId = :id"),
        @NamedQuery(name = "updateBirthDate", query = "update UserExtended ue set birthDate = :date  where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "updateFirstName", query = "update UserExtended ue set firstName = :firstName  where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "updateLastName", query = "update UserExtended ue set lastName = :lastName  where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "updateBgImage", query = "update UserExtended ue set bgImage = :image  where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "updateAbout", query = "update UserExtended ue set about = :about where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "updateEmail", query = "update UserExtended ue set email = :email where userExtendedId in (select userExtendedId from UserInfo where userId = :id)"),
        @NamedQuery(name = "getExtendedByUser", query = "select u.userExtended from UserInfo u where u.userId = :id"),
        @NamedQuery(name = "countUsers", query = "select distinct count(u.userId) from UserInfo u")
})
public class UserInfo {

    public static final String GET_SMALL_USER = "getSmallUser";
    public static final String GET_SMALL_USERS = "getSmallUsers";
    public static final String GET_COMMON_USER = "getCommonUser";
    public static final String GET_FULL_USER = "getFullUser";
    public static final String DELETE_USER = "deleteUser";
    public static final String FIND_SMALL_USERS = "findSmallUsers";
    public static final String FIND_COMMON_USERS = "findCommonUsers";
    public static final String GET_USERS_BY_BIRTHDATE = "getUsersByBirthDate";
    public static final String GET_USERS_EMAILS_BY_DATE = "getUsersEmailsByDate";
    public static final String CHECK_USER = "checkUser";
    public static final String GET_SMALL_USER_BY_LOGIN = "getSmallUserByLogin";
    public static final String UPDATE_IMAGE = "updateImage";
    public static final String UPDATE_BIRTHDATE = "updateBirthDate";
    public static final String UPDATE_FIRSTNAME = "updateFirstName";
    public static final String UPDATE_LASTNAME = "updateLastName";
    public static final String UPDATE_BGIMAGE = "updateBgImage";
    public static final String UPDATE_ABOUT = "updateAbout";
    public static final String UPDATE_EMAIl = "updateEmail";
    public static final String UPDATE_LOGIN = "updateLogin";
    public static final String GET_USER_EXTENDED = "getExtendedByUser";
    public static final String COUNT_USERS = "countUsers";

    private long userId;
    private String login;
    private long rating = 0;
    private Set<Questions> userQuestions;
    private Set<ArticleComments> userArticleComments;
    private Set<ArticleInfo> userArticles;
    private Set<Answer> answers;
    private Set<UserInbox> notices;
    private Set<UsersTags> interests;
    private Set<ProblemsUsers> problems;
    private Set<Tags> tags;
    private Set<Facts> facts;
    private UserCounter userCounter;

    private UserExtended userExtended;

    private LogInfo logInfo;
    private SecuredToken securedToken;

    private boolean activated = false;

    private String smallImagePath;
    private String middleImagePath;
    private String largeImagePath;

    public UserInfo(){}

    public UserInfo(String login, String smallImagePath, String middleImagePath, String largeImagePath){
        this.login = login;
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
        activated = true;
        userCounter = new UserCounter();
    }


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "login", length = 32)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "rating")
    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return userId == userInfo.userId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, login, rating);
    }



    @OneToMany(mappedBy = "userInfo", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<Questions> getUserQuestions() {
        return this.userQuestions;
    }

    public void setUserQuestions(Set<Questions> userQuestions) {
        this.userQuestions = userQuestions;
    }

    @OneToMany(mappedBy = "userInfo", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<ArticleComments> getUserArticleComments() {
        return this.userArticleComments;
    }

    public void setUserArticleComments(Set<ArticleComments> userArticleComments) {
        this.userArticleComments = userArticleComments;
    }

    @OneToMany(mappedBy = "userInfo", cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<ArticleInfo> getUserArticles() {
        return this.userArticles;
    }

    public void setUserArticles(Set<ArticleInfo> userArticles) {
        this.userArticles = userArticles;
    }

    @OneToMany(mappedBy = "userInfo", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }


    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "log_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "logInfo_fk"))
    public LogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_extended_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "user_extended_fk"))
    public UserExtended getUserExtended() {
        return userExtended;
    }

    public void setUserExtended(UserExtended userExtended) {
        this.userExtended = userExtended;
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

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @BatchSize(size = 10)
    public Set<UserInbox> getNotices() {
        return notices;
    }

    public void setNotices(Set<UserInbox> notices) {
        this.notices = notices;
    }

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = true)
    public SecuredToken getSecuredToken() {
        return securedToken;
    }

    public void setSecuredToken(SecuredToken securedToken) {
        this.securedToken = securedToken;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
    @BatchSize(size = 10)
    public Set<UsersTags> getInterests() {
        return interests;
    }

    public void setInterests(Set<UsersTags> interests) {
        this.interests = interests;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
    @BatchSize(size = 10)
    public Set<ProblemsUsers> getProblems() {
        return problems;
    }

    public void setProblems(Set<ProblemsUsers> problems) {
        this.problems = problems;
    }

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_counter_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "user_counter_fk"))
    public UserCounter getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(UserCounter userCounter) {
        this.userCounter = userCounter;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "creator")
    @BatchSize(size = 10)
    public Set<Tags> getTags() {
        return tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "creator")
    @BatchSize(size = 10)
    public Set<Facts> getFacts() {
        return facts;
    }

    public void setFacts(Set<Facts> facts) {
        this.facts = facts;
    }
}
