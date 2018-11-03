package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;

public interface SettingsReadService {

    SettingsAccountTransfer getAccountSettingsTransfer(long userId);

    SettingsUserTransfer getUserSettingsTransfer(long userId);

    SettingsNotificationsTransfer getUserNotificationsTransfer(long userId);
}
