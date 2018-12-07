package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;

public interface UserReadService {

    FullUserTransfer getFullUser(long id);

    String getUsername(long id);

    PageableContainer getSmallUsers(String page, String type, String sort);

    PageableContainer getCommonUsers(String page, String type, String sort);

    PageableContainer getUsers(String page,String query, String type, String sort);

    PageableContainer findCommonUsers(String page, String search, String type, String sort);

    PageableContainer findSmallUsers(String page, String query, String type, String sort);

    PageableContainer getUsersByInterests(String page, String size, long tagId, String query, String type, String sort);
}
