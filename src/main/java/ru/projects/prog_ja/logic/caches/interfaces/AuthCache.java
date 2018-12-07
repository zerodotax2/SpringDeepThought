package ru.projects.prog_ja.logic.caches.interfaces;

import ru.projects.prog_ja.dto.auth.RestoreMessage;
import ru.projects.prog_ja.dto.auth.UpdateEmail;

public interface AuthCache {

    boolean putRestoreMessage(RestoreMessage message);

    RestoreMessage pollRestoreMessage(String token);

    boolean putUpdateEmailMessage(UpdateEmail updateEmail);

    UpdateEmail pollUpdateEmailMessage(String token);

    boolean containsRestoreToken(String token);

}
