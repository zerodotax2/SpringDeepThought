package ru.projects.prog_ja.logic.services.transactional.implementations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.SettingsReadService;
import ru.projects.prog_ja.model.dao.SettingsDAO;


@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SettingsReadServiceImpl implements SettingsReadService {

    private final SettingsDAO settingsDAO;

    @Autowired
    public SettingsReadServiceImpl(SettingsDAO settingsDAO) {
        this.settingsDAO = settingsDAO;
    }

    @Override
    public SettingsAccountTransfer getAccountSettingsTransfer(long userId) {
        return settingsDAO.getSettingsAccount(userId);
    }

    @Override
    public SettingsUserTransfer getUserSettingsTransfer(long userId) {
        return settingsDAO.getSettingsUser(userId);
    }

    @Override
    public SettingsNotificationsTransfer getUserNotificationsTransfer(long userId) {
        return settingsDAO.getSettingsNotifications(userId);
    }
}
