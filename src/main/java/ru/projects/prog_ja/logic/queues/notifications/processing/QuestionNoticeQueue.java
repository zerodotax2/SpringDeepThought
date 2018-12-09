package ru.projects.prog_ja.logic.queues.notifications.processing;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.messages.QuestionNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.templates.QuestionTemplates;
import ru.projects.prog_ja.model.dao.QuestionsDAO;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope("singleton")
public class QuestionNoticeQueue extends AbstractNotificationQueue<QuestionNoticeMessage>
     implements ProcessingNoticeQueue<QuestionNoticeMessage> {

    private final UserDAO userDAO;
    private final QuestionsDAO questionsDAO;

    public QuestionNoticeQueue(NotificationQueue notificationQueue, UserDAO userDAO, QuestionsDAO questionsDAO) {
        super(notificationQueue);
        this.userDAO = userDAO;
        this.questionsDAO = questionsDAO;
    }

    @Override
    public boolean putMessage(QuestionNoticeMessage message) {

       return messages.add(message);

    }

    @Scheduled(fixedRate = 60_000)
    public void sendAll(){
        sendMessages();
    }

    @Override
    protected NotificationMessage send(QuestionNoticeMessage message) throws Exception {

        switch (message.getType()){

            case RATE:
                return rateMessage(message);

            case ANSWER:
                return answerMessage(message);

            case ANSWER_RATE:
                return answerRateMessage(message);

            case RIGHT:
                return rightMessage(message);

        }

        return new NotificationMessage(0, "", NoticeType.ERROR);
    }

    private NotificationMessage rateMessage(QuestionNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = questionsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(QuestionTemplates.RATE_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.RATE);
    }

    private NotificationMessage answerMessage(QuestionNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = questionsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(QuestionTemplates.ANSWER_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.COMMENT);
    }

    private NotificationMessage answerRateMessage(QuestionNoticeMessage message){

        NoticeCommentTemplateDTO noticeTemplate = questionsDAO.getNoticeCommentTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(QuestionTemplates.ANSWER_RATE_TEMPLATE, message.getUserID(), commenter,
                noticeTemplate.getId(), newTitle);

        return new NotificationMessage(noticeTemplate.getUserId(), noticeMessage, NoticeType.RATE);
    }

    private NotificationMessage rightMessage(QuestionNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = questionsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(QuestionTemplates.RIGHT_ANSWER_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.INFO);
    }
}
