package ru.projects.prog_ja.model.entity.facts;


import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Facts")
@NamedQueries({
        @NamedQuery(name = "deleteFact", query = "delete from Facts f where f.factId = :id"),
        @NamedQuery(name = "getFact", query = "select f from Facts f" +
                " left join fetch f.tags t " +
                " left join fetch t.tagId " +
                " where f.factId = :id"),
        @NamedQuery(name = "getFacts", query = "select f from Facts f "),
        @NamedQuery(name = "updateFactRate", query = "update Facts set rating = rating + :rate where factId = :id"),
        @NamedQuery(name = "getCommonFacts", query = "select new ru.projects.prog_ja.dto.commons.CommonFactTransfer(" +
                " f.factId, f.text " +
                "   ) from Facts f"),
        @NamedQuery(name = "getFullFact", query = "select new ru.projects.prog_ja.dto.full.FullFactTransfer(" +
                " f.factId, f.text, u.userId, u.login, u.smallImagePath, u.rating  " +
                "   ) from Facts f " +
                " left join f.creator u " +
                " where f.factId = :id"),
        @NamedQuery(name = "countFacts", query = "select distinct count(f.factId) from Facts f"),
        @NamedQuery(name = "countByFactsTag", query = "select distinct count(f.factId) from FactsTags f where f.tagId = :tag "),
        @NamedQuery(name = "getFactByTag", query = "select f from Facts f " +
                " left join fetch f.tags t " +
                " left join fetch t.tagId " +
                " where t.tagId = :tag "),
        @NamedQuery(name = "getFactText", query = "select f.text from Facts f where f.factId = :id"),
        @NamedQuery(name = "updateFactOwnerRate", query = "update UserInfo set rating = rating + :rate " +
                " where :fact in elements(facts)"),
        @NamedQuery(name = "getFactNoticeTemplate", query = "select new ru.projects.prog_ja.dto.NoticeEntityTemplateDTO(" +
                " c.userId, f.text " +
                " ) from Facts f left join f.creator c where f.factId = :id"),
        @NamedQuery(name = "getTagsByFact", query = "select t.tagId from Facts f left join f.tags ft left join ft.tagId t where f.factId = :id"),
        @NamedQuery(name = "getUpdateFactEntity", query = "select f from Facts f " +
                " left join fetch f.tags " +
                " where f.factId = :id"),
        @NamedQuery(name = "removeFactTags", query = "delete from FactsTags where tagId in (:tags)"),
        @NamedQuery(name = "getTagsByFactId", query = "select new ru.projects.prog_ja.dto.smalls.SmallTagTransfer(t.tagId, t.name, t.color)" +
                " from Tags t left join t.facts f where f.factId = :fact")
})
public class Facts {

    private String text;
    private long factId;
    private Set<FactsTags> tags;
    private Date createDate;
    private long rating;

    private UserInfo creator;

    private boolean activated = false;

    @Column(name = "text")
    @Type(type = "org.hibernate.type.TextType")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fact_id", nullable = false, unique = true)
    public long getFactId() {
        return factId;
    }

    public void setFactId(long factId) {
        this.factId = factId;
    }

    public Facts(){}

    public Facts(String text) {
        this.text = text;
        createDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facts facts = (Facts) o;
        return factId == facts.factId &&
                Objects.equals(text, facts.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(text, factId);
    }

    @Override
    public String toString(){
        String result = "Факт: " + this.getText() + "\n Теги: ";
        for(FactsTags tags : tags){
            result += " " + tags.getTagId().getName();
        }
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "factId", cascade = CascadeType.ALL)
    public Set<FactsTags> getTags() {
        return tags;
    }

    public void setTags(Set<FactsTags> tags) {
        this.tags = tags;
    }


    @Basic
    @Column(name = "activated", nullable = false)
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fact_user_creator_fk"))
    public UserInfo getCreator() {
        return creator;
    }

    public void setCreator(UserInfo creator) {
        this.creator = creator;
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
    @Column(name = "rating", nullable =false)
    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
