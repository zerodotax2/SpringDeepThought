package ru.projects.prog_ja.logic.queues.notifications.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.FactNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.templates.FactTemplates;
import ru.projects.prog_ja.model.dao.FactsDAO;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope("singleton")
public class FactNoticeQueue extends AbstractNotificationQueue<FactNoticeMessage>
        implements ProcessingNoticeQueue<FactNoticeMessage> {

    private final UserDAO userDAO;
    private final FactsDAO factsDAO;

    @Autowired
    public FactNoticeQueue(NotificationQueue notificationQueue,
                           UserDAO userDAO, FactsDAO factsDAO) {
        super(notificationQueue);
        this.userDAO = userDAO;
        this.factsDAO = factsDAO;
    }

    @Override
    public boolean putMessage(FactNoticeMessage message) {

        return messages.add(message);

    }

    @Scheduled(fixedRate = 60_000)
    public void sendAll(){
        sendMessages();
    }


    @Override
    protected NotificationMessage send(FactNoticeMessage message) throws Exception {

        switch (message.getType()){
            case RATE:
                return rateNotice(message);
        }

        return new NotificationMessage(0, "", NoticeType.ERROR);
    }

    private NotificationMessage rateNotice(FactNoticeMessage message){

        NoticeEntityTemplateDTO templateDTO =  factsDAO.getNoticeTemplate(message.getId());
        String commenter = userDAO.getUsername(message.getUserId());

        String noticeMessage = String.format(FactTemplates.RATE_TEMPLATE, message.getUserId(), commenter,
                templateDTO.getTitle());

        return new NotificationMessage(templateDTO.getId(), noticeMessage, NoticeType.RATE);
    }

}
