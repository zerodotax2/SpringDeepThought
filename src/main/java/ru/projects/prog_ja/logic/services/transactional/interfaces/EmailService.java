package ru.projects.prog_ja.logic.services.transactional.interfaces;

public interface EmailService {

    boolean sendRestoreEmail(String token, String email);

    boolean sendActivateEmail(String login, String email, String token);

    boolean sendConfirmEmail(String email, String token);
}
