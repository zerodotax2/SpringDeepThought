package ru.projects.prog_ja.logic.queues.notifications.templates;

public final class ProblemTemplates {

    public static final String RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил вашу задачу " +
                    "<a href=\"/problems/%d\">%s</a>";

    public static final String COMMENT_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оставил комментарий к вашей задаче " +
                    "<a href=\"/problems/%d\">%s</a>";

    public static final String COMMENT_RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил ваш комментарий к задаче " +
                    "<a href=\"/problems/%d\">%s</a>";

    public static final String FEEDBACK_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оставил пожелания к вашей задаче " +
                    "<a href=\"/problems/%d\">%s</a>";

}
