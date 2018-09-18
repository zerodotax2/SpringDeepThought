package ru.projects.prog_ja.view.dto;

public class ErrorDTO {

    private boolean error = false;
    private String message;

    public ErrorDTO(){}

    public ErrorDTO(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
