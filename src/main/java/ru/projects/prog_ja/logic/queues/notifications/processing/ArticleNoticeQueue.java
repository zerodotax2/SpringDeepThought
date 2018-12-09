package ru.projects.prog_ja.logic.queues.notifications.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.NoticeCommentTemplateDTO;
import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.NoticeType;
import ru.projects.prog_ja.logic.queues.notifications.NotificationQueue;
import ru.projects.prog_ja.logic.queues.notifications.messages.ArticleNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.messages.NotificationMessage;
import ru.projects.prog_ja.logic.queues.notifications.templates.ArticleTemplates;
import ru.projects.prog_ja.model.dao.ArticleDAO;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope("singleton")
public class ArticleNoticeQueue extends AbstractNotificationQueue<ArticleNoticeMessage>
        implements ProcessingNoticeQueue<ArticleNoticeMessage> {


    private ArticleDAO articleDAO;
    private UserDAO userDAO;

    @Autowired
    public ArticleNoticeQueue(NotificationQueue queue,
                              UserDAO userDAO,
                              ArticleDAO articleDAO) {
        super(queue);
        this.userDAO = userDAO;
        this.articleDAO = articleDAO;
    }

    @Override
    public boolean putMessage(ArticleNoticeMessage message) {

        return messages.add(message);

    }

    @Scheduled(fixedRate = 60_00)
    public void sendAll(){
        sendMessages();
    }

    /*
    * Метод будет вызываться каждые 60 секунд из абстракного класса
    * */
    @Override
    protected NotificationMessage send(ArticleNoticeMessage message) throws Exception{

        switch (message.getType()){
            case RATE:
                return rateNotice(message);

            case COMMENT:
                return commentNotice(message);

            case COMMENT_RATE:
                return commentRateNotice(message);

        }

        //never happened
        return new NotificationMessage(1, "", NoticeType.ERROR);
    }

    public NotificationMessage rateNotice(ArticleNoticeMessage message) throws Exception{

        NoticeEntityTemplateDTO noticeTemplate = articleDAO.getArticleNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());


        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ArticleTemplates.RATE_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.RATE);
    }

    private NotificationMessage commentNotice(ArticleNoticeMessage message) throws Exception{

        NoticeEntityTemplateDTO noticeTemplate = articleDAO.getArticleNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ArticleTemplates.COMMENT_TEMPLATE, message.getUserID(), commenter,
                message.getEntityID(), newTitle);

        return new NotificationMessage(noticeTemplate.getId(), noticeMessage, NoticeType.COMMENT);
    }

    private NotificationMessage commentRateNotice(ArticleNoticeMessage message) throws Exception{

        NoticeCommentTemplateDTO noticeTemplate = articleDAO.getCommentNoticeTemplate(message.getEntityID());
        String commenter = userDAO.getUsername(message.getUserID());

        String newTitle = noticeTemplate.getTitle();
        if(newTitle.length() > 50)
            newTitle = newTitle.substring(0, 50) + "...";

        String noticeMessage = String.format(ArticleTemplates.COMMENT_RATE_TEMPLATE, message.getUserID(),
                commenter, noticeTemplate.getId(), newTitle);

        return new NotificationMessage(noticeTemplate.getUserId(), noticeMessage, NoticeType.RATE);
    }
}
