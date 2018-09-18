package ru.projects.prog_ja.logic.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.projects.prog_ja.logic.services.interfaces.TokenService;
import ru.projects.prog_ja.logic.util.HashType;
import ru.projects.prog_ja.logic.util.HasherClass;
import ru.projects.prog_ja.model.dao.UserDAO;
import ru.projects.prog_ja.model.entity.user.SecuredToken;

@Service
@Scope("prototype")
public class TokenServiceImpl implements TokenService {

    /**
     * class to hash tokens using SHA_256 algorithm
     * */
    private HasherClass hasherClass;

    /**
     * class to get and put user tokens to database
     * */
    private UserDAO userDAO;

    public TokenServiceImpl(@Autowired UserDAO userDAO){
        this.hasherClass = new HasherClass(HashType.SHA_256);
        this.userDAO = userDAO;
    }

    /**
     * достаём для данного пользователя токен из базы,
     * соединяем его с солью пользователя и проверям на валидность
     * */
    @Override
    public boolean authenticateUserToken(long userId, String token) {

        SecuredToken securedToken = userDAO.getTokenByUser(userId);
        if(securedToken == null){
            return false;
        }

        String check = hasherClass.hash(securedToken.getSalt() + token);
        if(!check.equals(securedToken.getToken())){
            return false;
        }

        return true;
    }

    /**
     * метод для создания токена пользователя,
     * получаем рандомную соль, получаем рандомный токен,
     * хешируем их вместе и получаем токен для сохранения,
     * теперь, чтобы проверить пользователя надо иметь соль и токен из базы,
     * а также токен пользователя
     * */
    @Override
    public String createToken(long userId) {

        String salt = hasherClass.getSalt(256);
        String randomToken = hasherClass.getSalt(256);

        String securedToken = hasherClass.hash(salt + randomToken);
        if(!userDAO.saveToken(userId, salt, securedToken)){
            return null;
        }

        return randomToken;
    }

}
