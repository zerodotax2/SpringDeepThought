package ru.projects.prog_ja.model.dao;



import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.full.FullFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallArticleTransfer;

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
    List<CommonFactTransfer> getFacts(int start, int size, String orderField, int sort);

    List<CommonFactTransfer> findFacts(int start, int size, String query, String orderField, int sort);

    List<CommonFactTransfer> getFactsByTag(int start, int size, long tagID, String orderField, int sort);

    List<CommonFactTransfer> findFactsByTag(int start, int size, long tagID, String query, String orderField, int sort);

    List<CommonFactTransfer> getFactsByUser(int start, int size, long userId, String orderField, int sort);

    List<CommonFactTransfer> findFactsByUser(int start, int size, long userId, String query, String orderField, int sort);
    /**
    * @return all tags that exists in database
    * */
    List<CommonFactTransfer> getAllFacts();

    long getFactsNum();

    long countFactsByTag(long tagId);

    CommonFactTransfer getFact(long factId);

    FullFactTransfer getFullFact(long factId);

    CommonFactTransfer getFactByTag(long tagID, long offset);
}
