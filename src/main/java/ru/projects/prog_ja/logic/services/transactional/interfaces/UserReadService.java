package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;

import java.util.List;

public interface UserReadService {

    FullUserTransfer getFullUser(long id);

    List<SmallUserTransfer> getSmallUsers(int start, String type, String sort);

    List<CommonUserTransfer> getCommonUsers(int start, String type, String sort);

    List<SmallUserTransfer> getUsers(int start,String query, String type, String sort);

    List<CommonUserTransfer> findCommonUsers(int start, String search, String type, String sort);

    List<SmallUserTransfer> findSmallUsers(int start, String query, String type, String sort);

    BySomethingContainer getUsersByInterests(int start, String size, long tagId, String type, String sort);
}
