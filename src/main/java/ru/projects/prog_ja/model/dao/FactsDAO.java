package ru.projects.prog_ja.model.dao;


import ru.projects.prog_ja.dto.NoticeEntityTemplateDTO;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;

import java.util.List;


/**
 * interface that contains methods to work with Facts entity
 */
public interface FactsDAO {

    /**
    * Add fact to database
     * @param fact - fact text,
     * @param tags - tag list of fact
    */
    CommonFactTransfer addFact(String fact, List<Long> tags, long userId);

    String getText(long factId);

    NoticeEntityTemplateDTO getNoticeTemplate(long factId);

    /**
     * @param id - delete fact with this id
     */
    boolean deleteFact(long id, long userId);

    /**
     * update fact with this params
     * */
    boolean updateFact(long id, String fact, List<Long> tags, long userId);

    boolean updateFactRate(long factId, int rate, long userId);
    /**
     *
     * @return random facts from Database
     */
    PageableEntity getFacts(int start, int size, String orderField, int sort);

    PageableEntity findFacts(int start, int size, String query, String orderField, int sort);

    PageableEntity  getFactsByTag(int start, int size, long tagID, String query, String orderField, int sort);

    PageableEntity getFactsByUser(int start, int size, long userId, String query, String orderField, int sort);

    /**
    * @return all tags that exists in database
    * */
    List<CommonFactTransfer> getAllFacts();

    List<Long> getTagsByFact(long factId);

    long getFactsNum();

    long countFactsByTag(long tagId);

    CommonFactTransfer getFact(long factId);

    FullFactTransfer getFullFact(long factId);

    List<SmallTagTransfer> getTagsByFactID(long factId);

    CommonFactTransfer getFactByTag(long tagID, long offset);
}
