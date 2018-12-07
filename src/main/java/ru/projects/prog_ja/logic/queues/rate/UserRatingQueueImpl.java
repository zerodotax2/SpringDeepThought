package ru.projects.prog_ja.logic.queues.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.model.dao.RatingDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Scope("singleton")
public class UserRatingQueueImpl implements UserRatingQueue{

    private final Queue<UserRateMessage> userRateMessages = new ConcurrentLinkedQueue<>();
    private final Map<Integer, Boolean> fails = new HashMap<>();

    private final RatingDAO ratingDAO;

    @Autowired
    public UserRatingQueueImpl(RatingDAO ratingDAO) {
        this.ratingDAO = ratingDAO;
    }

    @Override
    public void putMessage(UserRateMessage userRateMessage){

        userRateMessages.add(userRateMessage);

    }

    /**
     * Если очередь пуста, то ждём минуту, иначе достаём сообщение
     * и по типу обновляем рейтинг пользователя.
     * При ошибке ложим снова в очередь, если ошибка второй раз, то удаляем сообщение
     * */
    @Scheduled(fixedRate = 60_000)
    public void addMessages(){

        UserRateMessage message;
        while((message = userRateMessages.poll()) != null){

            try {

                updateRate(message);

            }catch (Exception e){
                int hashCode = userRateMessages.hashCode();

                if(fails.containsKey(hashCode)){

                    fails.remove(hashCode);

                }else{

                    fails.put(hashCode, true);
                    userRateMessages.add(message);

                }
            }

        }

    }


    /**
     * В зависимости от типа выполняем изменение рейтинга пользователя,
     * в дальнейшем можно будет для каждого типа сообщений создать свою очередь,
     * что увеличит производительность
     * */

    private void updateRate(UserRateMessage message) throws Exception{

        switch (message.getType()){

            case USER:

                ratingDAO.updateUserRate(message.getEntityID(), message.getRate());
                break;
            case ARTICLE_OWNER:

                ratingDAO.updateArticleOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;
            case ARTICLE_COMMENT_OWNER:

                ratingDAO.updateArticleCommentOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case QUESTION_OWNER:

                ratingDAO.updateQuestionOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case ANSWER_OWNER:

                ratingDAO.updateAnswerOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case PROBLEM_OWNER:

                ratingDAO.updateProblemOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case PROBLEM_COMMENT_OWNER:

                ratingDAO.updateProblemCommentOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case TAG_OWNER:

                ratingDAO.updateTagOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            case FACT_OWNER:

                ratingDAO.updateFactOwnerRate(message.getEntityID(), message.getRate(), message.getUserId());
                break;

            default:
                break;
        }

    }




}
