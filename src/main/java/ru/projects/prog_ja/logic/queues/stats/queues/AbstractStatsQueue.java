package ru.projects.prog_ja.logic.queues.stats.queues;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.projects.prog_ja.model.dao.StatsDAO;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@EnableScheduling
public abstract class AbstractStatsQueue<T> implements StatsQueue<T>{

    protected final Queue<T> messages = new ConcurrentLinkedQueue<>();
    protected final Map<Integer, Boolean> fails = new ConcurrentHashMap<>();

    protected final StatsDAO statsDAO;

    public AbstractStatsQueue(StatsDAO statsDAO){

        this.statsDAO = statsDAO;
    }

    @Override
    public boolean putMessage(T message){

        return messages.add(message);
    }

    @Scheduled(fixedRate = 60_000)
    public void sendMessages(){

        T message;

        while((message = messages.poll()) != null){

            try {
                send(message);
            }catch (Exception e){

                int hashCode = message.hashCode();

                if(fails.containsKey(hashCode)){
                    fails.remove(hashCode);
                }else{
                    messages.add(message);
                    fails.put(hashCode, true);
                }
            }

        }

    }

    protected abstract void send(T message) throws Exception;

}
