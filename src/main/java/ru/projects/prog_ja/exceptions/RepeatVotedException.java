package ru.projects.prog_ja.exceptions;

public class RepeatVotedException extends Exception {

    public RepeatVotedException() {
    }

    public RepeatVotedException(String message) {
        super(message);
    }

    public RepeatVotedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatVotedException(Throwable cause) {
        super(cause);
    }

    public RepeatVotedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
