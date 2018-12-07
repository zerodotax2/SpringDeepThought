package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.queries.UserQueries;
import ru.projects.prog_ja.model.dao.NoticeDAO;
import ru.projects.prog_ja.model.dao.SettingsDAO;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

@Repository
@Scope("prototype")
public class HibernateSettingsDAOImpl extends GenericDAO implements SettingsDAO {

    private final NoticeDAO  noticeDAO;

    @Autowired
    public HibernateSettingsDAOImpl(SessionFactory sessionFactory,
                                    NoticeDAO noticeDAO) {
        super(sessionFactory);
        this.noticeDAO = noticeDAO;
    }

    @Override
    public SettingsUserTransfer getSettingsUser(long userId) {

        Session session = session();
        SettingsUserTransfer user = session.createNamedQuery(UserQueries.GET_SETTINGS_USER, SettingsUserTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);

        if(user != null) {
            UserInfo userInfo = session.load(UserInfo.class, userId);
            List<SmallTagTransfer> interests =
                    session.createNamedQuery(UserQueries.GET_USER_INTERESTS, SmallTagTransfer.class)
                    .setParameter("user", userInfo)
                    .getResultList();

             user.setInterests(interests != null ? interests : Collections.emptyList());
        }

        return user;
    }

    @Override
    public SettingsAccountTransfer getSettingsAccount(long userId) {
        return session().createNamedQuery(UserQueries.GET_SETTINGS_ACCOUNT, SettingsAccountTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public SettingsNotificationsTransfer getSettingsNotifications(long userId,int size, int page) {
        SettingsNotificationsTransfer user = session().createNamedQuery(UserQueries.GET_SETTINGS_NOTIFICATIONS, SettingsNotificationsTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }
        user.setNotifications(new TreeSet<>(noticeDAO.getNoticesByUser(userId, (page-1) * size, size)));

        return user;
    }
}
