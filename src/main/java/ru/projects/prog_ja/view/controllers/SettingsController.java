package ru.projects.prog_ja.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.SettingsAccountTransfer;
import ru.projects.prog_ja.dto.commons.SettingsNotificationsTransfer;
import ru.projects.prog_ja.dto.commons.SettingsUserTransfer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.SettingsReadService;
import ru.projects.prog_ja.exceptions.InternalServerException;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    private final SettingsReadService settingsReadService;

    private static final String SETTINGS_USER_DTO_NAME = "settingsUserDTO";
    private static final String SETTINGS_ACCOUNT_DTO_NAME = "settingsAccountDTO";
    private static final String SETTINGS_NOTIFICATIONS_DTO_NAME = "settingsNotificationsDTO";

    @Autowired
    public SettingsController(SettingsReadService settingsReadService){
        this.settingsReadService = settingsReadService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getUserSettings(@SessionAttribute("user") UserDTO userDTO) throws InternalServerException {

        SettingsUserTransfer settingsUserTransfer = settingsReadService.getUserSettingsTransfer(userDTO.getId());
        if(settingsUserTransfer == null){
            throw new InternalServerException();
        }

        return new ModelAndView("users/settingsUser",
                SETTINGS_USER_DTO_NAME, settingsUserTransfer);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView getAccountSettings(@SessionAttribute("user") UserDTO userDTO) throws InternalServerException {

        SettingsAccountTransfer settingsAccountTransfer = settingsReadService.getAccountSettingsTransfer(userDTO.getId());
        if(settingsAccountTransfer == null){
            throw new InternalServerException();
        }

        return new ModelAndView("users/settingsAccount",
                SETTINGS_ACCOUNT_DTO_NAME, settingsAccountTransfer);
    }

    @RequestMapping(value = "/notifications")
    public ModelAndView getUserNotifications(@SessionAttribute("user") UserDTO user) throws InternalServerException {

        SettingsNotificationsTransfer settingsNotificationsTransfer
                = settingsReadService.getUserNotificationsTransfer(user.getId());
        if(settingsNotificationsTransfer == null){
            throw new InternalServerException();
        }

        return new ModelAndView("users/settingsNotifications",
                SETTINGS_NOTIFICATIONS_DTO_NAME, settingsNotificationsTransfer);
    }
}
