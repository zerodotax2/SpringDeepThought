package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.Role;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.model.entity.tags.Tags;
import ru.projects.prog_ja.model.entity.user.UserCounter;
import ru.projects.prog_ja.model.entity.user.UserExtended;
import ru.projects.prog_ja.model.entity.user.UserInfo;
import ru.projects.prog_ja.model.entity.user.UsersTags;

import java.util.Set;

@Service
@Scope("prototype")
public class UserConverter {

    public FullUserTransfer fullUser(UserInfo user, UserExtended userExtended, UserCounter userCounter, Set<UsersTags> interests){


        FullUserTransfer fullUserTransfer = new FullUserTransfer(
                user.getUserId(), user.getLogin(), user.getLargeImagePath(), user.getRating(),
                userExtended.getFirstName(), userExtended.getLastName(), userExtended.getCreateDate(), userExtended.getBirthDate(), userExtended.getAbout(),
                userCounter.getArticles(), userCounter.getComments(),
                userCounter.getQuestions(), userCounter.getAnswers(),
                userCounter.getProblems(), userCounter.getDecided(),
                userCounter.getTags(), userCounter.getFacts()
        );

        if(interests != null){
            for(UsersTags interest1 : interests){
                Tags tag = interest1.getTagId();
                fullUserTransfer.getInterests().add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
            }
        }

        return fullUserTransfer;
    }

    public SmallUserTransfer smallUser(UserInfo user){

        return new SmallUserTransfer(user.getUserId(), user.getLogin(), user.getSmallImagePath(), user.getRating());
    }

    public UserDTO forumUser(UserInfo user, Role role, Set<UsersTags> tags, long notices){

        UserDTO userDTO = new UserDTO(
                user.getUserId(), user.getLogin(), user.getSmallImagePath(), user.getRating(), role);
        if(tags!= null){
            for (UsersTags usersTag : tags){
                Tags tag = usersTag.getTagId();
                userDTO.getPrefers().add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
            }
        }
        userDTO.setNotices(notices);

        return userDTO;
    }

}
