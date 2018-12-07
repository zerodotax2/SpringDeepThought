package ru.projects.prog_ja.logic.singletons.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.auth.MailHelper;
import ru.projects.prog_ja.logic.singletons.interfaces.EmailPaths;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("singleton")
public class EmailPathsImpl implements EmailPaths {

    private static final Map<String, MailHelper> mails
            = new ConcurrentHashMap<>(26);


    @PostConstruct
    public void initMap(){

        mails.put("mail.ru", new MailHelper("Почта Mail.Ru", "https://e.mail.ru/"));
        mails.put("bk.ru", new MailHelper("Почта Mail.Ru (bk.ru)", "https://e.mail.ru/"));
        mails.put("list.ru", new MailHelper("Почта Mail.Ru (list.ru)", "https://e.mail.ru/"));
        mails.put("inbox.ru", new MailHelper("Почта Mail.Ru (inbox.ru)", "https://e.mail.ru/"));
        mails.put("yandex.ru", new MailHelper("Яндекс.Почта", "https://mail.yandex.ru/"));
        mails.put("ya.ru", new MailHelper("Яндекс.Почта", "https://mail.yandex.ru/"));
        mails.put("yandex.ua", new MailHelper("Яндекс.Почта", "https://mail.yandex.ua/"));
        mails.put("yandex.by", new MailHelper("Яндекс.Почта", "https://mail.yandex.by/"));
        mails.put("yandex.kz", new MailHelper("Яндекс.Почта", "https://mail.yandex.kz/"));
        mails.put("yandex.com", new MailHelper("Яндекс.Почта", "https://mail.yandex.com/"));
        mails.put("gmail.com", new MailHelper("Gmail", "https://mail.google.com/"));
        mails.put("googlemail.com", new MailHelper("Gmail", "https://mail.google.com/"));
        mails.put("outlook.com", new MailHelper("Outlook.com", "https://mail.live.com/"));
        mails.put("hotmail.com", new MailHelper("Outlook.com (Hotmail)", "https://mail.live.com/"));
        mails.put("live.ru", new MailHelper("Outlook.com (live.ru)", "https://mail.live.com/"));
        mails.put("live.com", new MailHelper("Outlook.com (live.com)", "https://mail.live.com/"));
        mails.put("me.com", new MailHelper("iCloud Mail", "https://www.icloud.com/"));
        mails.put("icloud.com", new MailHelper("iCloud Mail", "https://www.icloud.com/"));
        mails.put("rambler.ru", new MailHelper("Рамблер-Почта", "https://mail.rambler.ru/"));
        mails.put("yahoo.com", new MailHelper("Yahoo! Mail", "https://mail.yahoo.com/"));
        mails.put("ukr.net", new MailHelper("Почта ukr.net", "https://mail.ukr.net/"));
        mails.put("i.ua", new MailHelper("Почта I.UA", "http://mail.i.ua/"));
        mails.put("bigmir.net", new MailHelper("Почта Bigmir.net", "http://mail.bigmir.net/"));
        mails.put("tut.by", new MailHelper("Почта tut.by", "https://mail.tut.by/"));
        mails.put("inbox.lv", new MailHelper("Inbox.lv", "https://www.inbox.lv/"));
        mails.put("mail.kz", new MailHelper("Почта mail.kz", "http://mail.kz/"));

    }

    @Override
    public MailHelper getLinkByEmail(String email) {

        String[] arr = email.split("@");
        if(arr.length != 2 || arr[1] == null)
            return null;

        return mails.get(arr[1]);
    }
}
