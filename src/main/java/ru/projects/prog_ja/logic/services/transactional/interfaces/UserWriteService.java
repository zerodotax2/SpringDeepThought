package ru.projects.prog_ja.logic.services.transactional.interfaces;

import java.util.List;

public interface UserWriteService {

    boolean updateUser(long id, String firstName, String lastName, String birthDate, String bgImage, String about, List<Long> interests);

    boolean updateFirstName(long id, String firstName);

    boolean updateLastName(long id, String lastName);

    boolean updateAbout(long id, String about);

    boolean updateInterests(long id, List<Long> tags);

    boolean updateBgImage(long id, String image);

    boolean updateUserImage(long id, List<String> mainImages);

    boolean updateBirthDate(long id, String date);
    
}
