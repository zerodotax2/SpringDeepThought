package ru.projects.prog_ja.model.dao;

import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;

public interface SettingsDAO {

    SettingsUserTransfer getSettingsUser(long userId);

    SettingsAccountTransfer getSettingsAccount(long userId);

    SettingsNotificationsTransfer getSettingsNotifications(long userId);

}
