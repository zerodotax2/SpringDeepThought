package ru.projects.prog_ja.logic.caches.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.model.dao.TagsDAO;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(scopeName = "singleton")
@EnableScheduling
public class TagsLocalCache implements TagsCache {

    /**
    * Карта для хранения всех тегов
    * */
    private static Map<Long, CommonTagTransfer> tags = new ConcurrentHashMap<>();

    /**
    * Лист текущих популярных тегов, постоянно обновляемый
    * */
    private static List<SmallTagTransfer> popularTags;

    private final TagsDAO tagsDAO;

    @Value("{tags.small.show.size}")
    private int smallTagsSize;

    public TagsLocalCache(@Autowired TagsDAO tagsDAO){
        this.tagsDAO = tagsDAO;
    }

    @PostConstruct
    public void init(){

        Map<Long, CommonTagTransfer> tagsTemp = new ConcurrentHashMap<>();

        /*Инициализируем все теги в коллекции*/
        tagsDAO.getAllTags().forEach((tag) -> {

            tagsTemp.put(tag.getId(),  tag);

        });

        tags = tagsTemp;

        /*Инициализируем самые популярные теги*/

        List<SmallTagTransfer> popularTagsTemp = tagsDAO.getSmallPopularTags(0, smallTagsSize);
        if(popularTagsTemp != null){
            popularTags = popularTagsTemp;
        }

    }

    @Scheduled(fixedRate = 3_600_000)
    public void updateCache(){
        init();
    }


    private static final Object putTagKey = new Object();

    @Override
    public void putTag(CommonTagTransfer tag) {

        synchronized (putTagKey){
            tags.put(tag.getId(), tag);
        }

    }

    /**
     * Защищаем от конкурентного доступа т.к. тут важно какой тег получишь
     * */
    @Override
    public CommonTagTransfer getCommonTagByID(long tagID){

        return tags.get(tagID);
    }

    @Override
    public List<SmallTagTransfer> getPopularTags() {

        return popularTags;
    }

    /**
     * reload popular tags every hour
     * */
    @Scheduled(fixedRate = 3_600_000)
    public void rePopularTags(){

        popularTags = tagsDAO.getSmallPopularTags(0, smallTagsSize);

    }

    private static final Object deleteTagKey = new Object();

    @Override
    public void deleteTag(long tagId){

        synchronized (deleteTagKey){
            tags.remove(tagId);
        }

    }

}
