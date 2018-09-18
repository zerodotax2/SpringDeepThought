package ru.projects.prog_ja.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractRestService extends ResponseMessages {

    /**
     * Метод получает поток байтов и с помощью javax.json
     * из JSON-B возвращает JsonObject
     * */
    protected JsonObject parseJSONtoObject(String jsonData){

       return Json.createParser(
                new InputStreamReader(
                        new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8
                )).getObject();
    }

    /**
     * Метод получает поток байтов и с помощью javax.json
     * из JSON-B возвращает JsonArray
     * */
    protected JsonArray parseJsonToArray(String jsonData){

        return Json.createParser(
                new InputStreamReader(
                        new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8
                )).getArray();
    }

}
