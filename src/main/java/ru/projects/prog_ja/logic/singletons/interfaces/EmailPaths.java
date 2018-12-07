package ru.projects.prog_ja.logic.singletons.interfaces;

import ru.projects.prog_ja.dto.auth.MailHelper;

public interface EmailPaths {

    MailHelper getLinkByEmail(String email);

}
