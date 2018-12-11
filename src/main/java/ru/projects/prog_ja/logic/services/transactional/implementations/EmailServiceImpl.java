package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.services.transactional.interfaces.EmailService;

import javax.mail.internet.MimeMessage;

@Service
@Scope("prototype")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private static String host;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendRestoreEmail(String token, String email) {

        try {
             MimeMessage message = mailSender.createMimeMessage();

             message.setContent("<html><body><p>Для восстановления доступа к аккаунту перейдите по следующей</p>" +
                     "<a href='"+host+"/restore/"+token+"'> ссылке</a></body></html>", "text/html;charset=utf-8");
             message.setSubject("Восстановление доступа", "UTF-8");

             MimeMessageHelper helper = new MimeMessageHelper(message, true);
             helper.setTo(email);

             mailSender.send(message);
         }catch (Exception e){
             return false;
         }

         return true;
    }

    @Override
    public boolean sendActivateEmail(String login, String email, String token) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setContent("<html><body><p>Добро пожаловать на форум, "+login+"</p>" +
                            "<p>Для окончания регистрации перейдите по следующей <a href='"+host+"/account/activate/"+token+"'>" +
                            "ссылке</a></p></body><html>"
                    , "text/html;charset=utf-8");
            message.setSubject("Активация аккаунта", "UTF-8");

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);

            mailSender.send(message);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean sendConfirmEmail(String email, String token) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setContent("<html><body>Для подтверждения email перейдите по следующей" +
                            " <a href='"+host+"/account/email/activate/"+token+"'>ссылке</a></body><html>"
                    , "text/html;charset=utf-8");
            message.setSubject("Подтверждение email", "UTF-8");

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);

            mailSender.send(message);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Value("${host}")
    public void setHost(String h){
        host = h;
    }
}
