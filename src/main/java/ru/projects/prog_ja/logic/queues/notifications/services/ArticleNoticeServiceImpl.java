package ru.projects.prog_ja.logic.queues.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.queues.notifications.messages.ArticleNoticeMessage;
import ru.projects.prog_ja.logic.queues.notifications.processing.ArticleNoticeQueue;

@Service
@Scope("prototype")
public class ArticleNoticeServiceImpl implements ArticleNoticeService{


    private final ArticleNoticeQueue queue;

    @Autowired
    public ArticleNoticeServiceImpl(ArticleNoticeQueue queue) {
        this.queue = queue;
    }

    @Override
    public void addRateNotice(long articleId, long userId) {

        queue.putMessage(new ArticleNoticeMessage(articleId, userId,
                ArticleNoticeMessage.Type.RATE));

    }
    @Override
    public void addCommentNotice(long articleId, long userId) {

        queue.putMessage(new ArticleNoticeMessage(articleId, userId,
                ArticleNoticeMessage.Type.COMMENT));

    }

    @Override
    public void addCommentRateNotice(long commentId, long userId) {

        queue.putMessage(new ArticleNoticeMessage(commentId, userId,
                ArticleNoticeMessage.Type.COMMENT_RATE));
    }

}
