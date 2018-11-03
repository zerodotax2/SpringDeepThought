package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;

import java.util.List;

public interface FactsReadService {

    CommonFactTransfer getRandomFact();

    CommonFactTransfer getNextFact(String factId, String rate, UserDTO userDTO);

    CommonFactTransfer getFactByTags(List<Long> tags);

    CommonFactTransfer getFactByTag(long id);

    FullFactTransfer getFullFact(long id);

    BySomethingContainer getFactsByTag(int start,String size, long tagID, String type, String sort);

    BySomethingContainer getFactsByUser(int start, String size, long userId, String type, String sort);

    List<CommonFactTransfer> getFacts(int start, String query, String type, String sort);

}
