package ru.projects.prog_ja.logic.queues.notifications.templates;

public final class ArticleTemplates {

    public static final String RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил вашу статью " +
                    "<a href=\"/articles/%d\">%s</a>";

    public static final String COMMENT_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оставил комментарий к вашей статье " +
                    "<a href=\"/articles/%d\">%s</a>";

    public static final String COMMENT_RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил ваш комментарий к статье " +
                    " <a href=\"/articles/%d\">%s</a>";
}
