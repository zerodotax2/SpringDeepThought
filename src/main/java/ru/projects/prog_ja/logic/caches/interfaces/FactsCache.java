package ru.projects.prog_ja.logic.caches.interfaces;

import ru.projects.prog_ja.dto.commons.CommonFactTransfer;


public interface FactsCache {

    /**
     * @return random fact
     * */
    CommonFactTransfer getFact(long factID);

    /**
     * @return fact by this tag
     * */
    CommonFactTransfer getFactByTag(long tagID, int positionInList);

    /**
     * @return facts cache size
     * */
    int cacheSize();

    /**
     * put fact into cache
     * */
    void putFact(CommonFactTransfer fact);

    /**
     * @return size of list with
     * @param tagID tag
     * */
    int factsByTagSize(long tagID);

    /**
     * delete fact from cache with
     * @param factID
     * */
    void deleteFactFromCache(long factID);
}
