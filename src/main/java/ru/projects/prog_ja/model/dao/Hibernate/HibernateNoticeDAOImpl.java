package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.queries.UserInboxQueries;
import ru.projects.prog_ja.model.dao.NoticeDAO;
import ru.projects.prog_ja.model.entity.user.UserInbox;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateNoticeDAOImpl extends GenericDAO implements NoticeDAO {

    public HibernateNoticeDAOImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<SmallNoticeTransfer> getNoticesByUser(long userId, int start, int size) {

        return session().createNamedQuery(UserInboxQueries.GET_ALL_USER_NOTICES, SmallNoticeTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .setParameter("user", userId)
                .getResultList();
    }

    @Override
    public List<SmallNoticeTransfer> getLastNoticesByUser(long userId, int start, int size) {

        return session().createNamedQuery(UserInboxQueries.GET_LAST_USER_NOTICES, SmallNoticeTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .setParameter("user", userId)
                .getResultList();
    }


    @Override
    public boolean addNoticeToUser(long userId, String message, NoticeType type) {

        try {
            Session session = session();

            UserInfo user = session.load(UserInfo.class, userId);

            UserInbox userInbox = new UserInbox(message, type);
            userInbox.setUserInfo(user);

            session.save(userInbox);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean unactivateNotice(long noticeId) {
        try {
            return session().createNamedQuery(UserInboxQueries.UNACTIVATE_NOTICE)
                    .setParameter("id", noticeId)
                    .executeUpdate() != 0;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean unactivateNotices(List<Long> notices) {
        try{
            Session session = session();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaUpdate<UserInbox> update = cb.createCriteriaUpdate(UserInbox.class);
            Root<UserInbox> root = update.from(UserInbox.class);

            update.set(root.get("active"), false);
            update.where(root.get("userInboxId").in(notices));

            return session.createQuery(update).executeUpdate() == notices.size();
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean setWatchedNotices(long userId) {
        Session session = session();
        return session.createNamedQuery(UserInboxQueries.UNACTIVATE_ALL_NOTICES)
                .setParameter("user", session.load(UserInfo.class, userId))
                .executeUpdate() != 0;
    }
}
