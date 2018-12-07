package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.projects.prog_ja.dto.DefaultDTO;

import java.util.*;

public abstract class GenericDAO {

    protected final SessionFactory sessionFactory;

    public GenericDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    protected Session session(){
        return this.sessionFactory.getCurrentSession();
    }

    protected <T extends DefaultDTO> Map<Long, T> toMap(List<T> entities){

        if(entities == null)
            return Collections.emptyMap();

        HashMap<Long, T> resultMap = new LinkedHashMap<>();

        for(T entity : entities){
            resultMap.put(entity.getId(), entity);
        }

        return resultMap;
    }

}
