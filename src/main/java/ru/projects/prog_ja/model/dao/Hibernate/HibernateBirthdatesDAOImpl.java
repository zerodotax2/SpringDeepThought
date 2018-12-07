package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.model.dao.BirthdatesDAO;
import ru.projects.prog_ja.model.dao.Hibernate.queries.UserQueries;

import java.sql.Date;
import java.util.List;

@Repository
@Scope(scopeName = "prototype")
public class HibernateBirthdatesDAOImpl extends GenericDAO implements BirthdatesDAO {

    public HibernateBirthdatesDAOImpl(@Autowired SessionFactory sessionFactory){
        super(sessionFactory);
    }

    @Override
    public List<CommonUserTransfer> getTodayUsers() {

        Query<CommonUserTransfer> query = session().createNamedQuery(UserQueries.GET_USERS_BY_BIRTHDATE, CommonUserTransfer.class)
                .setParameter("date", new java.util.Date());

        List<CommonUserTransfer> users = query.getResultList();

        return users;
    }

    @Override
    public List<String> getTodayEmails() {

        Query<String> query = session().createNamedQuery(UserQueries.GET_USERS_EMAILS_BY_DATE, String.class)
                .setParameter("date", new java.util.Date());

        List<String> emails = query.getResultList();

        return emails;
    }

    @Override
    public List<CommonUserTransfer> getDateUsers(Date date) {

        Query<CommonUserTransfer> query = session().createNamedQuery(UserQueries.GET_USERS_BY_BIRTHDATE, CommonUserTransfer.class)
                .setParameter("date", date);

        List<CommonUserTransfer> users = query.getResultList();

        return users;
    }

    @Override
    public List<String> getDateEmails(Date date) {

        Query<String> query = session().createNamedQuery(UserQueries.GET_USERS_EMAILS_BY_DATE, String.class)
                .setParameter("date", date);

        List<String> emails = query.getResultList();

        return emails;
    }

    @Override
    public boolean updateBirthdate(long userId, Date date) {

        try {
            return session().createSQLQuery("update UserInfo set birthDate = ? where user_id = '"+userId+"'")
                    .setParameter(1, date)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }


    }
}
