package ru.projects.prog_ja.dto;

public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER"), ROLE_MODER("ROLE_MODER");

    private final String name;

    Role(String s){
        this.name = s;
    }
    @Override
    public String toString(){
        return this.name;
    }

}
