package ru.projects.prog_ja.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.projects.prog_ja.dto.view.MessageType;
import ru.projects.prog_ja.dto.view.ResponseMessageDTO;

/**
 * Класс служит для возвращения ошибок HTTP запросов в REST сервисах
 * */
public abstract class ResponseMessages {

    /**
     * Истекло время ожидания
     * */
    protected ResponseEntity<ResponseMessageDTO> timeout(){

        return new ResponseEntity<>(new ResponseMessageDTO("Истекло время ожидания", MessageType.ERROR),
                HttpStatus.GATEWAY_TIMEOUT);
    }

    /**
     * Внутрення ошибка сервера
     * */
    protected ResponseEntity<ResponseMessageDTO> serverError(){

        return new ResponseEntity<>(new ResponseMessageDTO("Ошибка сервера", MessageType.ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
    * Некорректный формат данных
    * */
    protected ResponseEntity<ResponseMessageDTO> incorrectFormat(){

        return new ResponseEntity<>(new ResponseMessageDTO("Некорректный формат данных", MessageType.ERROR),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Доступ к ресурсу данным пользователем запрещен
     * */
    protected ResponseEntity<ResponseMessageDTO> accessDenied(){

        return new ResponseEntity<>(new ResponseMessageDTO("Доступ запрещён", MessageType.ERROR),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * Неправильно составленнй запрос
     * */
    protected ResponseEntity<ResponseMessageDTO> badRequest(){

        return new ResponseEntity<>(new ResponseMessageDTO("Неккоректный запрос", MessageType.ERROR),
                HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ResponseMessageDTO> error(String error){

        return new ResponseEntity<>(new ResponseMessageDTO(error, MessageType.ERROR),
                HttpStatus.CONFLICT);
    }

    /**
     * Объект запроса был успещно найден
     * */
    protected <T> ResponseEntity<T> found(T object){

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    /**
     * Объект был принят/создан
     * */
    protected <T> ResponseEntity<T> accepted(T object){

        return new ResponseEntity<>(object, HttpStatus.ACCEPTED);
    }

    /**
     * Операция выполнена успешно
     * */
    protected ResponseEntity ok(){

        return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
    }

    /**
     * Объект не был найден
     * */
    protected ResponseEntity<ResponseMessageDTO> notFound(){

        return new ResponseEntity<>(new ResponseMessageDTO("Не удалось найти ресурс", MessageType.ERROR),
                HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<ResponseMessageDTO> isFree(){

        return new ResponseEntity<>(new ResponseMessageDTO("Не удалось найти ресурс", MessageType.ERROR),
                HttpStatus.NO_CONTENT);
    }

    protected ResponseEntity<ResponseMessageDTO> already(){
        return new ResponseEntity<>(new ResponseMessageDTO("Вы уже делали это действие", MessageType.ERROR),
                HttpStatus.ALREADY_REPORTED);
    }
}
