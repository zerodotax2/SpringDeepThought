package ru.projects.prog_ja.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.prog_ja.dto.commons.CommonFactTransfer;
import ru.projects.prog_ja.logic.caches.interfaces.FactsCache;
import ru.projects.prog_ja.services.AbstractRestService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/services/facts")
public class RestFactsService extends AbstractRestService {

    private FactsCache factsCache;

    public RestFactsService(@Autowired FactsCache factsCache){
        this.factsCache = factsCache;
    }

    @GetMapping
    public ResponseEntity<?> getRandomFact() throws InterruptedException, ExecutionException, TimeoutException {

        /*
        * Вовзращаем рандомный факт из синглтона
        * */
        CommonFactTransfer factTransfer = factsCache.getFact().get(5, TimeUnit.SECONDS);
        if(factTransfer == null){
            return serverError();
        }

        return found(factTransfer);
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFactByTag(@PathVariable("id") String strID) throws InterruptedException, ExecutionException, TimeoutException {
        /*
        * Проверям id на валидность
        * */
        if(!strID.matches("^\\d+$") || strID.length() > 64){
            return incorrectFormat();
        }
        /*
        * Если id валидно, то возвращаем тег по id
        * */
        CommonFactTransfer factTransfer = factsCache.getFactByTag(Long.parseLong(strID)).get(5, TimeUnit.SECONDS);
        if(factTransfer == null){
            return serverError();
        }

        return found(factTransfer);
    }
}
