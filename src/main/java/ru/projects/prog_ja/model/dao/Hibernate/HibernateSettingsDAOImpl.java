package ru.projects.prog_ja.model.dao.Hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.dao.NoticeDAO;
import ru.projects.prog_ja.model.dao.SettingsDAO;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.TreeSet;

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
        return session().createNamedQuery(UserInfo.GET_SETTINGS_USER, SettingsUserTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public SettingsAccountTransfer getSettingsAccount(long userId) {
        return session().createNamedQuery(UserInfo.GET_SETTINGS_ACCOUNT, SettingsAccountTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public SettingsNotificationsTransfer getSettingsNotifications(long userId) {
        SettingsNotificationsTransfer user = session().createNamedQuery(UserInfo.GET_SETTINGS_NOTIFICATIONS, SettingsNotificationsTransfer.class)
                .setParameter("id" , userId)
                .getResultList().stream().findFirst().orElse(null);
        if(user == null){
            return null;
        }
        user.setNotifications(new TreeSet<>(noticeDAO.getNoticesByUser(userId, 0, 10)));

        return user;
    }
}
