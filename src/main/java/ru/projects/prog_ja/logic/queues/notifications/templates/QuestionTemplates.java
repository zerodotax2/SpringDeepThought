package ru.projects.prog_ja.logic.queues.notifications.templates;

public final class QuestionTemplates {

    public static final String RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил ваш вопрос " +
                    "<a href=\"/questions/%d\"> %s</a>";


    public static final String ANSWER_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оставил ответ к вашему вопросу " +
                    "<a href=\"/questions/%d\"> %s</a>";

    public static final String ANSWER_RATE_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> оценил ваш ответ к вопросу " +
                    " <a href=\"/questions/%d\">%s</a>";

    public static final String RIGHT_ANSWER_TEMPLATE =
            "<a href=\"/users/%d\">%s</a> отметил ваш ответ правильным к вопросу " +
                    " <a href=\"/questions/%d\">%s</a>";
}
