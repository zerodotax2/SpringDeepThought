package ru.projects.prog_ja.model.entity.user;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.articles.ArticleComments;
import ru.projects.prog_ja.model.entity.articles.ArticleInfo;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.problems.Problem;
import ru.projects.prog_ja.model.entity.problems.ProblemComment;
import ru.projects.prog_ja.model.entity.problems.ProblemFeedback;
import ru.projects.prog_ja.model.entity.problems.ProblemsSolvedUsers;
import ru.projects.prog_ja.model.entity.questions.Answer;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.support.UserForumAnswer;
import ru.projects.prog_ja.model.entity.support.UserQuestion;
import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "UserInfo")
@NamedQueries({
        @NamedQuery(name = "getSmallUser", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " where u.userId = :id"),
        @NamedQuery(name = "getSmallUsers", query = "select new ru.projects.prog_ja.dto.smalls.SmallUserTransfer( u.userId, u.login, u.smallImagePath, u.rating ) from UserInfo u " +
                " order by u.rating desc "),
        @NamedQuery(name = "getSmallUserByLogin", query = "select u from UserInfo u " +
                " left join fetch u.logInfo l " +
                " left join fetch l.role " +
                " left join fetch u.interests ui " +
                " left join fetch ui.tagId " +
                " left join fetch u.notices " +
                " where u.login = :login"),
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
        @NamedQuery(name = "getUsersEmailsByDate", query = "select l.email from LogInfo l left join l.userInfo u left join u.userExtended ue where ue.birthDate = :date"),
        @NamedQuery(name = "getFullUser", query = "select u " +
                " from UserInfo u " +
                " left join fetch u.userCounter " +
                " left join fetch u.userExtended " +
                " left join fetch u.interests as i" +
                " left join fetch i.tagId  " +
                " left join fetch u.securedToken " +
                " left join fetch u.logInfo l " +
                " left join fetch l.role " +
                " where u.userId = :id"),
        @NamedQuery(name = "checkUser", query = "select u from UserInfo u " +
                " left join fetch u.logInfo l " +
                " left join fetch l.role " +
                " left join fetch u.interests ui " +
                " left join fetch ui.tagId " +
                " where l.login = :login and l.passH = :pass"),
        @NamedQuery(name = "getSettingsUser", query = "select new ru.projects.prog_ja.dto.commons.SettingsUserTransfer (" +
                " u.userId, u.login, u.smallImagePath, u.rating, ue.firstName, ue.lastName, ue.about, ue.birthDate  " +
                " ) from UserInfo u" +
                " left join u.userExtended ue  " +
                " where u.userId = :id"),
        @NamedQuery(name = "getSettingsAccount", query = "select new ru.projects.prog_ja.dto.commons.SettingsAccountTransfer (" +
                " u.userId, u.login, u.smallImagePath, u.rating, ui.firstName, ui.lastName, l.email " +
                " ) from UserInfo u" +
                " left join u.userExtended ui" +
                " left join u.logInfo l  " +
                " where u.userId = :id"),
        @NamedQuery(name = "getSettingsNotifications", query = "select new ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer (" +
                " u.userId, u.login, u.smallImagePath, u.rating, ue.firstName, ue.lastName, uc.notices" +
                " ) from UserInfo u" +
                " left join u.userExtended ue " +
                " left join u.userCounter uc " +
                " where u.userId = :id"),
        @NamedQuery(name = "updateImage", query = "update UserInfo set smallImagePath = :small, middleImagePath = :middle, largeImagePath = :large where userId = :id"),
        @NamedQuery(name = "updateBirthDate", query = "update UserExtended set birthDate = :date  where user = :user"),
        @NamedQuery(name = "updateFirstName", query = "update UserExtended  set firstName = :firstName  where user = :user"),
        @NamedQuery(name = "updateLastName", query = "update UserExtended  set lastName = :lastName  where user = :user"),
        @NamedQuery(name = "updateBgImage", query = "update UserExtended  set bgImage = :image  where user = :user"),
        @NamedQuery(name = "updateAbout", query = "update UserExtended  set about = :about where user = :user"),
        @NamedQuery(name = "updateRate", query = "update UserInfo u set u.rating = u.rating + :rate where userId = :id"),
        @NamedQuery(name = "updateEmail", query = "update LogInfo set email = :email where userInfo = :user"),
        @NamedQuery(name = "getExtendedByUser", query = "select u.userExtended from UserInfo u where u.userId = :id"),
        @NamedQuery(name = "countUsers", query = "select distinct count(u.userId) from UserInfo u"),
        @NamedQuery(name = "countUserNotices", query = "select distinct count(ui.userInboxId) from UserInbox ui left join ui.userInfo u where u.userId=:id and ui.active = true"),
        @NamedQuery(name = "getUsername", query = "select login from UserInfo u where userId = :id"),
        @NamedQuery(name = "updatePassword", query = "update LogInfo set passH = :pass where userInfo = :user"),
        @NamedQuery(name = "getTokenByUserId", query = "select u.securedToken from UserInfo as u where u.userId = :id"),
        @NamedQuery(name = "getTagsByUser", query = "select t.tagId from UserInfo u left join u.interests ut left join ut.tagId t where u.userId = :id"),
        @NamedQuery(name = "getUserInterests", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer(" +
                " t.tagId, t.name, t.color " +
                ") from UsersTags ut left join ut.tagId t where ut.userId = :user"),
        @NamedQuery(name = "removeUserTags", query = "delete from UsersTags where tagId in (:tags)"),
        @NamedQuery(name = "getUserIdByEmail", query = "select u.userId from LogInfo l left join l.userInfo u where l.email = :email"),
        @NamedQuery(name = "getAuthByEmail", query = "select new ru.projects.prog_ja.dto.auth.AuthDTO(u.userId, l.login, l.email, l.enable)" +
                " from LogInfo l left join l.userInfo u where l.email = :email"),
        @NamedQuery(name = "getAuthById", query = "select new ru.projects.prog_ja.dto.auth.AuthDTO(u.userId, l.login, l.email, l.enable)" +
                " from LogInfo l left join l.userInfo u where u.userId = :id"),
        @NamedQuery(name = "getUserByActivateToken", query = "select at from ActivateTokens at where token = :token"),
        @NamedQuery(name = "activateAccount", query = "update LogInfo set enable = true where token = :token "),
        @NamedQuery(name = "getLogIDByEmail", query = "select logId from LogInfo where email = :email"),
        @NamedQuery(name = "getLogTokenByEmail", query = "select l.logId, t.activateTokenId from LogInfo l left join l.token t where l.email = :email"),
        @NamedQuery(name = "checkLogin", query = "select logId from LogInfo where login = :login")
})
@DynamicUpdate
public class UserInfo {

    private long userId;
    private String login;
    private long rating;
    private Set<Questions> userQuestions;
    private Set<ArticleComments> userArticleComments;
    private Set<ProblemComment> userProblemComments;
    private Set<ArticleInfo> userArticles;
    private Set<Answer> answers;
    private Set<UserInbox> notices;
    private Set<UsersTags> interests;
    private Set<Problem> problems;
    private Set<Tags> tags;
    private Set<Facts> facts;
    private Set<ProblemsSolvedUsers> solved;
    private Set<UserQuestion> userForumQuestions;
    private Set<UserForumAnswer> userForumAnswers;
    private Set<ProblemFeedback> userProblemFeedbacks;
    private UserCounter userCounter;

    private UserExtended userExtended;

    private LogInfo logInfo;
    private SecuredToken securedToken;

    private boolean activated = false;

    private String smallImagePath = "files/00/00/00/user-small.png";
    private String middleImagePath = "files/00/00/00/user-middle.png";
    private String largeImagePath = "files/00/00/00/user-large.png";

    public UserInfo(){}

    public UserInfo(String login, String smallImagePath, String middleImagePath, String largeImagePath){
        this.login = login;
        this.smallImagePath = smallImagePath;
        this.middleImagePath = middleImagePath;
        this.largeImagePath = largeImagePath;
        activated = true;
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


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userInfo", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    public LogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    public SecuredToken getSecuredToken() {
        return securedToken;
    }

    public void setSecuredToken(SecuredToken securedToken) {
        this.securedToken = securedToken;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    public Set<UsersTags> getInterests() {
        return interests;
    }

    public void setInterests(Set<UsersTags> interests) {
        this.interests = interests;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @BatchSize(size = 10)
    public Set<Problem> getProblems() {
        return problems;
    }

    public void setProblems(Set<Problem> problems) {
        this.problems = problems;
    }

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "user")
    @BatchSize(size = 10)
    public Set<ProblemsSolvedUsers> getSolved() {
        return solved;
    }

    public void setSolved(Set<ProblemsSolvedUsers> solved) {
        this.solved = solved;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "user", orphanRemoval = true)
    @BatchSize(size = 10)
    public Set<UserQuestion> getUserForumQuestions() {
        return userForumQuestions;
    }

    public void setUserForumQuestions(Set<UserQuestion> userForumQuestions) {
        this.userForumQuestions = userForumQuestions;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "user", orphanRemoval = true)
    @BatchSize(size = 10)
    public Set<UserForumAnswer> getUserForumAnswers() {
        return userForumAnswers;
    }

    public void setUserForumAnswers(Set<UserForumAnswer> userForumAnswers) {
        this.userForumAnswers = userForumAnswers;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "userInfo", orphanRemoval = true)
    @BatchSize(size = 10)
    public Set<ProblemFeedback> getUserProblemFeedbacks() {
        return userProblemFeedbacks;
    }

    public void setUserProblemFeedbacks(Set<ProblemFeedback> userProblemFeedbacks) {
        this.userProblemFeedbacks = userProblemFeedbacks;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "creator")
    @BatchSize(size = 10)
    public Set<ProblemComment> getUserProblemComments() {
        return userProblemComments;
    }

    public void setUserProblemComments(Set<ProblemComment> userProblemComments) {
        this.userProblemComments = userProblemComments;
    }
}
