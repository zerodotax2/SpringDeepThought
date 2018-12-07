package ru.projects.prog_ja.logic.queues.notifications.processing;

import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public abstract class AbstractNotificationQueue<T> {

    protected final Queue<T> messages = new ConcurrentLinkedQueue<>();
    protected final Map<Integer, Boolean> fails = new HashMap<>();

    protected final NotificationQueue notificationQueue;

    public AbstractNotificationQueue(NotificationQueue notificationQueue) {
        this.notificationQueue = notificationQueue;
    }

    protected void sendMessages(){

        while (!messages.isEmpty()){

            T message = messages.poll();

            try {

                notificationQueue.putMessage( send(message) );

            }catch (Exception e){

                int hashCode = message.hashCode();

                if(fails.containsKey(hashCode))
                    fails.remove(hashCode);
                else {
                    fails.put(hashCode, true);
                    messages.add(message);
                }
            }
        }

    }

    protected abstract NotificationMessage send(T message) throws Exception;
}
