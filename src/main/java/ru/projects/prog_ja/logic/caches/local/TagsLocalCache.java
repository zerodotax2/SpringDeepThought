package ru.projects.prog_ja.logic.caches.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonTagTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.logic.caches.interfaces.TagsCache;
import ru.projects.prog_ja.model.dao.TagsDAO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(scopeName = "singleton")
public class TagsLocalCache implements TagsCache {

    /**
    * Карта для хранения всех тегов
    * */
    private static Map<Long, CommonTagTransfer> tags = new ConcurrentHashMap<>();

    /**
    * Лист текущих популярных тегов, постоянно обновляемый
    * */
    private static List<SmallTagTransfer> popularTags = new ArrayList<>();

    private final TagsDAO tagsDAO;

    private static int smallTagsSize;

    @Autowired
    public TagsLocalCache(TagsDAO tagsDAO){
        this.tagsDAO = tagsDAO;
    }

    @PostConstruct
    public void init(){

        Map<Long, CommonTagTransfer> tagsTemp = new ConcurrentHashMap<>();

        List<CommonTagTransfer> tagTransfers = tagsDAO.getAllTags();
        /*Инициализируем все теги в коллекции*/
        tagTransfers.forEach((tag) -> {

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


    @Override
    public void putTag(CommonTagTransfer tag) {

            tags.put(tag.getId(), tag);

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

    @Override
    public void deleteTag(long tagId){

            tags.remove(tagId);

    }

    @Value("${tags.small.show.size}")
    public void setSmallTagsSize(int size) {
        smallTagsSize = size;
    }
}
