package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.model.dao.UserDAO;

@Service
@Scope(scopeName = "prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserReadServiceImpl implements UserReadService {


    private static int usersSmallSize;
    private static int usersCommonSize;
    private static int maxEntitiesSize;

    private final UserDAO userDAO;
    private final ValuesParser parser;

    @Autowired
    public UserReadServiceImpl(UserDAO userDAO,
                               ValuesParser parser){
        this.userDAO = userDAO;
        this.parser = parser;
    }


    @Override
    public PageableContainer getSmallUsers(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                userDAO.getSmallUsers((parsedPage-1)*usersSmallSize, usersSmallSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(), 
                parser.getPages(parsedPage, pageable.getCount(), usersSmallSize));
    }

    private PageableContainer getModers(String page, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable = userDAO.getModers((parsedPage-1)*usersSmallSize, usersSmallSize, getOrderField(type), parser.getSort(sort));
        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), usersSmallSize));

    }
    private PageableContainer findModers(String page, String query, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                userDAO.findModers((parsedPage-1)*usersSmallSize, usersSmallSize, query, getOrderField(type), parser.getSort(sort));
        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), usersSmallSize));

    }

    @Override
    public PageableContainer getUsers(String page, String query, String type, String sort){
        if(query != null && RegexUtil.string(query).matches()){
            if(type != null && type.equals("moder"))
                return findModers(page, query, type, sort);
            return findSmallUsers(page, query, type, sort);
        }else{
            if(type != null && type.equals("moder"))
                return getModers(page, type, sort);
            return getSmallUsers(page, type, sort);
        }
    }

    @Override
    public PageableContainer findSmallUsers(String page, String query, String type, String sort) {

        if(query == null || !RegexUtil.string(query).matches())
            return getSmallUsers(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                userDAO.findUsers((parsedPage-1)*usersSmallSize, usersSmallSize, query, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), usersSmallSize));
    }

    @Override
    public FullUserTransfer getFullUser(long id) {

        return userDAO.getFullUser(id);
    }

    @Override
    public String getUsername(long id) {

        String name = userDAO.getUsername(id);

        return name == null ? "" : name;
    }

    @Override
    public PageableContainer getCommonUsers(String page, String type, String sort) {

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                userDAO.getCommonUsers((parsedPage-1)*usersCommonSize, usersCommonSize, getOrderField(type), parser.getSort(sort));
      
        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), usersCommonSize));
    }

    @Override
    public PageableContainer findCommonUsers(String page, String search, String type, String sort) {

        if(search == null || !RegexUtil.string(search).matches())
            return getCommonUsers(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                userDAO.findCommonUsers((parsedPage-1)*usersCommonSize, usersCommonSize, search, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), usersCommonSize));
    }

    @Override
    public PageableContainer getUsersByInterests(String page, String size, long tagId, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = userDAO.getSmallUsersByTag((parsedPage-1)*parsedSize, parsedSize+1,
                tagId, parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }
    

    private String getOrderField(String type){

        if(type == null)
            return "rating";

        switch (type) {
            case "rating":
                return "rating";

            case "new":
                return "createDate";

            default:
                return "rating";
        }
    }

    @Value("${users.small.show.size}")
    public void setUsersSmallSize(int size) {
        usersSmallSize = size;
    }

    @Value("${users.common.show.size}")
    public void setUsersCommonSize(int size) {
        usersCommonSize = size;
    }

    @Value("${entities.max.size}")
    public void setMaxEntitiesSize(int size) {
        maxEntitiesSize = size;
    }
}
