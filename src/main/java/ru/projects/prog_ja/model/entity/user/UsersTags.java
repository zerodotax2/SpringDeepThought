package ru.projects.prog_ja.model.entity.user;

import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.*;

@Entity
@Table(name = "UsersTags")
public class UsersTags implements Comparable<UsersTags>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_tags_id", unique = true, nullable = false)
    private long usersTagsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserInfo userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tag_id", nullable = false)
    private Tags tagId;

    public UsersTags() {
    }

    public UsersTags(UserInfo userId, Tags tagId) {
        this.userId = userId;
        this.tagId = tagId;
    }

    public long getUsersTagsId() {
        return usersTagsId;
    }

    public void setUsersTagsId(long usersTagsId) {
        this.usersTagsId = usersTagsId;
    }

    public UserInfo getUserIdId() {
        return userId;
    }

    public void setUserId(UserInfo userId) {
        this.userId = userId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }

    @Override
    public int compareTo(UsersTags o) {
        return Long.compare(usersTagsId, o.getUsersTagsId());
    }
}
