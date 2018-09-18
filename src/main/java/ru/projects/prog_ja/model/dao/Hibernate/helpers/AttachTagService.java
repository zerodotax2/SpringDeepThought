package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.TagsContainer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.GenericDAO;
import ru.projects.prog_ja.model.entity.tags.Tags;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
@Scope("prototype")
public class AttachTagService<TT> extends GenericDAO {

    @Autowired
    public AttachTagService(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public <T extends TagsContainer>  void attachTags(Map<Long, T> entities, String idColumn, String entitiesName){

        Session session = session();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Tags> root = query.from(Tags.class);

        Join<Tags, TT> entityJoin = root.join(entitiesName, JoinType.LEFT);

        query.multiselect(entityJoin.get(idColumn), root.get("tagId"), root.get("name"), root.get("color"));
        query.where(entityJoin.get(idColumn).in(entities.keySet()));
        query.groupBy(entityJoin.get(idColumn), root.get("tagId"));

        List<Object[]> rows = session.createQuery(query).getResultList();
        for(Object[] row : rows){
            entities.get((long) row[0]).getTags().add(new SmallTagTransfer(
                    (long) row[1], (String) row[2], (String) row[3]
            ));
        }
    }

    public <T extends TagsContainer> List<T> tags(List<T> elements, String idColumn, String entitiesName){

        Map<Long, T> map = toMap(elements);
        attachTags(map, idColumn, entitiesName);

        return new ArrayList<>(map.values());
    }

}
