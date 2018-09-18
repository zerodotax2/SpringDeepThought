package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.dto.smalls.SmallTagTransfer;
import ru.projects.prog_ja.model.entity.facts.Facts;
import ru.projects.prog_ja.model.entity.facts.FactsTags;
import ru.projects.prog_ja.model.entity.tags.Tags;

import java.util.HashSet;
import java.util.Set;

@Service
@Scope("prototype")
public class FactConverter {

    public CommonFactTransfer commonFact(Facts fact, Set<FactsTags> factsTags){

        CommonFactTransfer commonFactTransfer = new CommonFactTransfer(fact.getFactId(), fact.getText());

        Set<SmallTagTransfer> tags = new HashSet<>();
        for(FactsTags tag1 : factsTags){
            Tags tag = tag1.getTagId();
            tags.add(new SmallTagTransfer(tag.getTagId(), tag.getName(), tag.getColor()));
        }
        commonFactTransfer.setTags(tags);

        return commonFactTransfer;
    }
}
