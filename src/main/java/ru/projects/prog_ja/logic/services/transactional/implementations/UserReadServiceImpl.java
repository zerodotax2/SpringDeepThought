package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.UserDTO;
import ru.projects.prog_ja.dto.commons.CommonUserTransfer;
import ru.projects.prog_ja.dto.full.FullUserTransfer;
import ru.projects.prog_ja.dto.smalls.SmallUserTransfer;
import ru.projects.prog_ja.dto.view.BySomethingContainer;
import ru.projects.prog_ja.logic.services.transactional.interfaces.UserReadService;
import ru.projects.prog_ja.model.dao.UserDAO;

import javax.validation.Valid;
import java.util.List;

@Service
@Scope(scopeName = "prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserReadServiceImpl implements UserReadService {


    @Value("${users.small.show.size}")
    private static int usersSmallSize;

    @Value("${users.common.show.size}")
    private static  int usersCommonSize;

    @Value("{entities.max.size}")
    private static int maxEntitiesSize;

    private final UserDAO userDAO;

    @Autowired
    public UserReadServiceImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }


    @Override
    public List<SmallUserTransfer> getSmallUsers(int start, String type, String sort) {

        return userDAO.getSmallUsers(start, usersSmallSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<SmallUserTransfer> getUsers(int start, String query, String type, String sort){
        if(query != null && !query.equals("")){
            if(type.equals("moder"))
                return userDAO.findModers(start, usersSmallSize, query, getOrderField(type), getSort(sort));
            return findSmallUsers(start, query, type, sort);
        }else{
            if(type.equals("moder"))
                return userDAO.getModers(start, usersSmallSize, getOrderField(type), getSort(sort));
            return getSmallUsers(start, type, sort);
        }
    }

    @Override
    public List<SmallUserTransfer> findSmallUsers(int start, String query, String type, String sort) {

        if(!query.matches("^[\\w|\\s]+$"))
            return getSmallUsers(start, type, sort);

        return userDAO.findUsers(start, usersSmallSize, query, getOrderField(type), getSort(sort));
    }

    @Override
    public FullUserTransfer getFullUser(long id) {
        return userDAO.getFullUser(id);
    }

    @Override
    public List<CommonUserTransfer> getCommonUsers(int start, String type, String sort) {

        return userDAO.getCommonUsers(start, usersCommonSize, getOrderField(type), getSort(sort));
    }

    @Override
    public List<CommonUserTransfer> findCommonUsers(int start, String search, String type, String sort) {

        if(!search.matches("^[\\w|\\s]+$"))
            return getCommonUsers(start, type, sort);

        return userDAO.findCommonUsers(start, usersCommonSize, search, getOrderField(type), getSort(sort));
    }

    @Override
    public BySomethingContainer getUsersByInterests(int start, String size, long tagId, String type, String sort) {

        int parsedSize = getSize(size);
        List<SmallUserTransfer> users = userDAO.getSmallUsersByTag(start, parsedSize+1,
                tagId, getOrderField(type), getSort(sort));

        return users != null ? new BySomethingContainer(users.size() > parsedSize, users) : null;
    }

    private int getSize(String s){
        try {
            int i = Math.abs(Integer.parseInt(s));
            return i > maxEntitiesSize ? 6 : i;
        }catch (NumberFormatException e){
            return 6;
        }
    }

    private String getOrderField(String type){
        switch (type) {
            case "rating":
                return "rating";

            case "new":
                return "createDate";

            default:
                return "rating";
        }
    }
    private int getSort(String s){
        return "0".equals(s) ? 0 : 1;
    }
}
