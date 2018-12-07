package ru.projects.prog_ja.logic.queues.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.stats.services.UserCounter;
import ru.projects.prog_ja.model.dao.NoticeDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Scope("singleton")
public class NotificationQueueImpl implements NotificationQueue {

    private final Queue<NotificationMessage> notifications = new ConcurrentLinkedQueue<>();
    private final Map<Integer, Boolean> fails = new HashMap<>();
    private final NoticeDAO noticeDAO;
    private final UserCounter userCounter;

    @Autowired
    public NotificationQueueImpl(NoticeDAO noticeDAO,
                                 UserCounter userCounter) {
        this.noticeDAO = noticeDAO;
        this.userCounter = userCounter;
    }


    @Override
    public void putMessage(NotificationMessage message) {

        notifications.add(message);

    }

    @Scheduled(fixedRate = 60_00)
    public void addNotifications(){


        while (!notifications.isEmpty()){

            NotificationMessage message = notifications.poll();

            if(!noticeDAO.addNoticeToUser(
                    message.getUserId(), message.getMessage(), message.getType())){

                int hashCode = message.hashCode();
                if(fails.containsKey(hashCode))

                    fails.remove(hashCode);
                else{

                    fails.put(hashCode, true);
                    notifications.add(message);
                }
            }else{
                userCounter.incrementNotices(message.getUserId(), 1);
            }

        }

    }


}
