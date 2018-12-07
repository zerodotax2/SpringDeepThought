package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;

import java.util.List;

public interface FactsReadService {

    CommonFactTransfer getRandomFact();

    CommonFactTransfer getNextFact(String factId, String rate, UserDTO userDTO);

    CommonFactTransfer getFactByTags(List<Long> tags);

    CommonFactTransfer getFactByTag(long id);

    FullFactTransfer getFullFact(long id);

    PageableContainer getFactsByTag(String page,String size, long tagID, String query, String type, String sort);

    PageableContainer getFactsByUser(String page, String size, long userId, String query, String type, String sort);

    PageableContainer getFacts(String page, String query, String type, String sort);

}
