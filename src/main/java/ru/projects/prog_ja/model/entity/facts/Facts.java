package ru.projects.prog_ja.model.entity.facts;


import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedQuery;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Facts")
@org.hibernate.annotations.NamedQueries({
        @NamedQuery(name = "deleteFact", query = "delete from Facts f where f.factId = :id"),
        @NamedQuery(name = "getFact", query = "select f from Facts f" +
                " left join fetch f.tags t " +
                " left join fetch t.tagId " +
                " where f.factId = :id"),
        @NamedQuery(name = "getFacts", query = "select f from Facts f "),
        @NamedQuery(name = "getCommonFacts", query = "select new ru.projects.prog_ja.dto.commons.CommonFactTransfer(" +
                " f.factId, f.text " +
                "   ) from Facts f"),
        @NamedQuery(name = "countFacts", query = "select distinct count(f.factId) from Facts f"),
        @NamedQuery(name = "countByFactsTag", query = "select distinct count(f.factId) from FactsTags f where f.tagId = :tag "),
        @NamedQuery(name = "getFactByTag", query = "select f from Facts f " +
                " left join fetch f.tags t " +
                " left join fetch t.tagId " +
                " where t.tagId = :tag ")
})
public class Facts {

    public static final String DELETE_FACT = "deleteFact";
    public static final String GET_FACT = "getFact";
    public static final String GET_FACTS = "getFacts";
    public static final String GET_COMMON_FACTS = "getCommonFacts";
    public static final String COUNT_FACTS = "countFacts";
    public static final String COUNT_FACTS_BY_TAG = "countFactsByTag";
    public static final String GET_FACT_BY_TAG = "getFactByTag";

    private String text;
    private long factId;
    private Set<FactsTags> tags;
    private Date createDate;

    private UserInfo creator;

    private boolean activated = false;

    @Column(name = "text")
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "factId")
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
}
