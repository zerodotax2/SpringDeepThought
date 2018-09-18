package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonAnswerTransfer;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.entity.answer.Answer;
import ru.projects.prog_ja.model.entity.questions.QuestionContent;
import ru.projects.prog_ja.model.entity.questions.Questions;
import ru.projects.prog_ja.model.entity.questions.QuestionsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Service
@Scope("prototype")
public class QuestionConverter {

    public FullQuestionTransfer fullQuestion(Questions question,QuestionContent content, Set<Answer> answers,  Set<QuestionsTags> tags,  UserInfo user){

        FullQuestionTransfer fullQuestionTransfer = new FullQuestionTransfer(
                question.getQuestionId(), question.getTitle(), question.getCreateDate(), question.getRating(),
                new SmallUserTransfer(user.getUserId(), user.getLogin(), user.getSmallImagePath(), user.getRating()),
                content.getHtmlContent()
        );

        Set<CommonAnswerTransfer> answerTransfers = new TreeSet<>();
        for(Answer answer : answers){
            UserInfo answerUser = answer.getUserInfo();

            answerTransfers.add(new CommonAnswerTransfer(
                    answer.getAnswerId(), answer.getHtmlContent(), answer.getRating(), answer.isRight(), answer.getCreateDate(),
                    new SmallUserTransfer(answer.getAnswerId(), answerUser.getLogin(), answerUser.getSmallImagePath(), answerUser.getRating())
            ));
        }
        fullQuestionTransfer.setAnswers(answerTransfers);

        for(QuestionsTags tag1 : tags){
            Tags tag = tag1.getTagId();
            fullQuestionTransfer.getTags().add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
        }

        return fullQuestionTransfer;
    }
}
