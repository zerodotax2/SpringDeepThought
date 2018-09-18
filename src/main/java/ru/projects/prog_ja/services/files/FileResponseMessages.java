package ru.projects.prog_ja.services.files;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.projects.prog_ja.dto.view.MessageType;
import ru.projects.prog_ja.dto.view.ResponseMessageDTO;
import ru.projects.prog_ja.services.AbstractRestService;

/**
 * Класс содержит HTTP ответы для File сервисов
 * */
public abstract class FileResponseMessages extends AbstractRestService {


    /**
     * Неккоректное имя файла
     * */
    protected  ResponseEntity<?> incorrectName(){

        return new ResponseEntity<>(new ResponseMessageDTO("Некорректное имя файла",
                MessageType.ERROR), HttpStatus.BAD_REQUEST);
    }

    /**
     * Файл уже существует
     * */

    protected  ResponseEntity<?> exist(){

        return new ResponseEntity<>(new ResponseMessageDTO("Файл уже существует",
                MessageType.ERROR), HttpStatus.CONFLICT);
    }

    /**
     * Неправильный тип загрузки файла
     * */
    protected  ResponseEntity<?> incorrectUploadType(){

        return new ResponseEntity<>(new ResponseMessageDTO("Неккоректный тип загрузки файла",
                MessageType.ERROR), HttpStatus.CONFLICT);
    }

}
