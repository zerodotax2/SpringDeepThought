package ru.projects.prog_ja.dto.view;

public enum MessageType {
    INFO("INFO"),
    ERROR("ERROR"),
    UNKNOWN("UNKNOWN");

    private final String str;

    MessageType(String str){
        this.str = str;
    }

    @Override
    public String toString(){
        return str;
    }
}
