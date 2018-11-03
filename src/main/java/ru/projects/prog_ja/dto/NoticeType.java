package ru.projects.prog_ja.dto;

public enum NoticeType {

    INFO("INFO"), MESSAGE("MESSAGE"), ERROR("ERROR");

    private final String name;

     NoticeType(String s){
        this.name = s;
    }

    @Override
    public String toString(){
         return this.name;
    }

}
