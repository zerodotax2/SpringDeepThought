package ru.projects.prog_ja.logic.caches.local;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.auth.RestoreMessage;
import ru.projects.prog_ja.dto.auth.RestoreQueueToken;
import ru.projects.prog_ja.dto.auth.UpdateEmail;
import ru.projects.prog_ja.dto.auth.UpdateEmailToken;
import ru.projects.prog_ja.logic.caches.interfaces.AuthCache;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Scope("singleton")
public class AuthLocalCache implements AuthCache {

    private final Queue<RestoreQueueToken> restoreQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, RestoreMessage> restoreMessages = new ConcurrentHashMap<>();

    private final Queue<UpdateEmailToken> updateEmailQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, UpdateEmail> updateEmailMessages = new ConcurrentHashMap<>();

    @Override
    public boolean putRestoreMessage(RestoreMessage message) {

        return restoreQueue.add(new RestoreQueueToken(new Date(),message.getToken()))
                && restoreMessages.put(message.getToken(), message) != null;
    }

    @Override
    public RestoreMessage pollRestoreMessage(String token) {

        return restoreMessages.get(token);
    }

    @Override
    public boolean containsRestoreToken(String token) {

        return restoreMessages.containsKey(token);
    }

    @Override
    public boolean putUpdateEmailMessage(UpdateEmail updateEmail) {

        updateEmailMessages.put(updateEmail.getToken(), updateEmail);

        return  updateEmailQueue.add(new UpdateEmailToken(new Date(), updateEmail.getToken()));
    }

    @Override
    public UpdateEmail pollUpdateEmailMessage(String token) {

        return updateEmailMessages.get(token);
    }

    @Scheduled(fixedRate = 600_000)
    public void clearLastRestoreMessages(){

       Date current = new Date();
       current.setTime((long)(current.getTime() - 3600_1000));

       RestoreQueueToken token;
       while (!restoreQueue.isEmpty()){

           token = restoreQueue.peek();

           if(token.getDate().after(current))
               break;
           else {
               restoreQueue.remove();
               restoreMessages.remove(token.getToken());
           }
       }

    }

    @Scheduled(fixedRate = 600_000)
    public void clearLastUpdateEmailMessages(){

       Date current = new Date();
       current.setTime((long)(current.getTime() - 3600_1000));

       UpdateEmailToken token;
       while (!updateEmailQueue.isEmpty()){

           token = updateEmailQueue.peek();

           if(token.getDate().after(current))
               break;
           else {
               updateEmailQueue.remove();
               updateEmailMessages.remove(token.getToken());
           }
       }

    }

}
