package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserWriteService;
import ru.projects.prog_ja.model.dao.UserDAO;

import java.sql.Date;
import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.REQUIRED)
public class UserWriteServiceImpl implements UserWriteService {


    private final UserDAO userDAO;

    @Autowired
    public UserWriteServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * updating user image/images in database with
     * @param id
     * */


    @Override
    public boolean updateUser(long id, String firstName, String lastName, String birthDate, String bgImage, String about, List<Long> interests){

      return userDAO.updateUser(id, firstName, lastName, java.sql.Date.valueOf(birthDate.replace(".", "-")), bgImage, about, interests);
    }

    @Override
    public boolean updateUserImage(long id, List<String> mainImages){
      return   userDAO.updateImage(id, mainImages);
    }

    @Override
    public boolean updateFirstName(long id, String firstName) {
       return userDAO.updateFirstName(id, firstName);
    }

    @Override
    public boolean updateLastName(long id, String lastName) {
      return   userDAO.updateLastName(id, lastName);
    }

    @Override
    public boolean updateAbout(long id, String about) {
       return userDAO.updateAbout(id, about);
    }

    @Override
    public boolean updateInterests(long id, List<Long> tags) {
       return userDAO.updateInterests(id, tags);
    }

    @Override
    public boolean updateBgImage(long id, String image) {
       return userDAO.updateBGImage(id, image);
    }

    @Override
    public boolean updateBirthDate(long id, String date) throws IllegalArgumentException{
        return userDAO.updateBirthdate(id, Date.valueOf(date));
    }
}
