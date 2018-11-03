package ru.projects.prog_ja.exceptions;

public class NonAuthorizedException extends Exception {

    public NonAuthorizedException() {
    }

    public NonAuthorizedException(String message) {
        super(message);
    }

    public NonAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonAuthorizedException(Throwable cause) {
        super(cause);
    }

    public NonAuthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
