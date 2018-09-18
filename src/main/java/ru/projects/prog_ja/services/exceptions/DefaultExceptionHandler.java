package ru.projects.prog_ja.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.projects.prog_ja.dto.view.MessageType;
import ru.projects.prog_ja.dto.view.ResponseMessageDTO;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {TimeoutException.class, InterruptedException.class, ExecutionException.class})
    @ResponseBody
    public ResponseEntity<?> asyncException(){

        return new ResponseEntity<>(new ResponseMessageDTO("Истекло время ожидания", MessageType.ERROR),
                HttpStatus.GATEWAY_TIMEOUT);
    }

}
