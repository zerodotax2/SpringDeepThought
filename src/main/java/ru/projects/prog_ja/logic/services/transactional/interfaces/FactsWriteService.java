package ru.projects.prog_ja.logic.services.transactional.interfaces;

import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.exceptions.BadRequestException;

import java.util.List;

public interface FactsWriteService {

    CommonFactTransfer createFact(String text, List<Long> tags, long userId);

    boolean updateFact(long factId, String text, List<Long> tags, long userId);

    boolean updateFactRate(long factId, int rate, long userId);

    boolean deleteFact(long factId, long userId);
}
