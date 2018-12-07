package ru.projects.prog_ja.exceptions;

public class NonActiveUserException extends RuntimeException {

    public NonActiveUserException() {
    }

    public NonActiveUserException(String message) {
        super(message);
    }

    public NonActiveUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonActiveUserException(Throwable cause) {
        super(cause);
    }

    public NonActiveUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
