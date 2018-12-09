package ru.projects.prog_ja.logic.queues.notifications.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.messages.ProblemNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.templates.ProblemTemplates;
import ru.projects.prog_ja.model.dao.ProblemsDAO;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope("singleton")
@EnableScheduling
public class ProblemNoticeQueue extends AbstractNotificationQueue<ProblemNoticeMessage>
        implements ProcessingNoticeQueue<ProblemNoticeMessage> {

    private final ProblemsDAO problemsDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProblemNoticeQueue(NotificationQueue notificationQueue,
                              ProblemsDAO problemsDAO, UserDAO userDAO) {
        super(notificationQueue);
        this.problemsDAO = problemsDAO;
        this.userDAO = userDAO;
    }

    @Override
    public boolean putMessage(ProblemNoticeMessage message) {

        return messages.add(message);

    }

    @Scheduled(fixedRate = 60_000)
    public void sendAll(){
        sendMessages();
    }

    @Override
    protected NotificationMessage send(ProblemNoticeMessage message) throws Exception {

        switch (message.getType()){
            case RATE:
                return rateNotice(message);

            case COMMENT:
                return commentNotice(message);

            case COMMENT_RATE:
                return commentRateNotice(message);

            case FEEDBACK:
                return feedbackNotice(message);
        }

        return new NotificationMessage(0, "", NoticeType.ERROR);
    }

    private NotificationMessage rateNotice(ProblemNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = problemsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ProblemTemplates.RATE_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.RATE);
    }

    private NotificationMessage commentNotice(ProblemNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = problemsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ProblemTemplates.COMMENT_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.COMMENT);

    }

    private NotificationMessage commentRateNotice(ProblemNoticeMessage message){

        NoticeCommentTemplateDTO noticeTemplate = problemsDAO.getNoticeCommentTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ProblemTemplates.COMMENT_RATE_TEMPLATE, message.getUserID(), commenter,
                noticeTemplate.getId(), newTitle);

        return new NotificationMessage(noticeTemplate.getUserId(), noticeMessage, NoticeType.RATE);

    }

    private NotificationMessage feedbackNotice(ProblemNoticeMessage message){

        NoticeEntityTemplateDTO noticeTemplate = problemsDAO.getNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ProblemTemplates.FEEDBACK_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.INFO);

    }
}
