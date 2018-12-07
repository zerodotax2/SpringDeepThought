package ru.projects.prog_ja.logic.queues.notifications.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.ForumNotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.templates.ForumTemplates;
import ru.projects.prog_ja.model.dao.SupportDAO;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope("singleton")
public class ForumNoticeQueue extends AbstractNotificationQueue<ForumNotificationMessage>
        implements ProcessingNoticeQueue<ForumNotificationMessage> {

    private final SupportDAO supportDAO;
    private final UserDAO userDAO;

    @Autowired
    public ForumNoticeQueue(NotificationQueue notificationQueue,
                            SupportDAO supportDAO, UserDAO userDAO) {
        super(notificationQueue);
        this.supportDAO = supportDAO;
        this.userDAO = userDAO;
    }

    @Override
    public boolean putMessage(ForumNotificationMessage message) {

        return messages.add(message);

    }

    @Scheduled(fixedRate = 60_000)
    public void sendAll(){
        sendMessages();
    }

    @Override
    protected NotificationMessage send(ForumNotificationMessage message) throws Exception {

        switch (message.getType()){

            case CREATE:
                return createNotice(message);

            case FORUM_ANSWER:
                return forumAnswer(message);
        }

        return new NotificationMessage(0, "", NoticeType.ERROR);
    }

    private NotificationMessage createNotice(ForumNotificationMessage message){


        return new NotificationMessage(message.getUserID(), ForumTemplates.CREATE_TEMPLATE, NoticeType.INFO);
    }

    private NotificationMessage forumAnswer(ForumNotificationMessage message){

        NoticeEntityTemplateDTO noticeTemplate = supportDAO.getNoticeTemplate(message.getEntityID());
        String answerer = userDAO.getUsername(message.getUserID());

        String noticeMessage = String.format(ForumTemplates.FORUM_ANSWER_TEMPLATE, message.getUserID(),
                answerer, message.getEntityID());

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.INFO);
    }

}
