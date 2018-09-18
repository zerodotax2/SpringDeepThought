package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.smalls.SmallNoticeTransfer;
import ru.projects.prog_ja.model.dao.NoticeDAO;
import ru.projects.prog_ja.model.entity.user.NoticeType;
import ru.projects.prog_ja.model.entity.user.UserInbox;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.Collections;
import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateNoticeDAOImpl extends GenericDAO implements NoticeDAO {

    public HibernateNoticeDAOImpl(@Autowired SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<SmallNoticeTransfer> getAllNoticesByUser(long userId, int start, int size) {

        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null){
            return Collections.emptyList();
        }

        return session.createNamedQuery(UserInbox.GET_ALL_USER_NOTICES, SmallNoticeTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<SmallNoticeTransfer> getLastNoticesByUser(long userId, int start, int size) {
        Session session = session();
        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null){
            return Collections.emptyList();
        }

        return session.createNamedQuery(UserInbox.GET_ALL_USER_NOTICES, SmallNoticeTransfer.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public boolean addNoticeToUser(long userId, String title, String message, NoticeType type) {

        Session session = session();

        UserInfo user = session.load(UserInfo.class, userId);
        if(user == null){
            return false;
        }

        UserInbox userInbox = new UserInbox(title, message, type);
        userInbox.setUserInfo(user);

        session.save(userInbox);

        return true;
    }

    @Override
    public void unactivateNotice(long noticeId) {
        session().createNamedQuery(UserInbox.UNACTIVATE_NOTICE)
                .setParameter("id", noticeId)
                .executeUpdate();
    }
}
