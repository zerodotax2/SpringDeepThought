package ru.projects.prog_ja.logic.services.simple.implementations;

public enum HashType {

    SHA_256("SHA-256"), MD5("MD5"), BCRYPT("BCrypt");

    private final String name;

    HashType(String s){
        this.name = s;
    }

    @Override
    public String toString(){
        return name;
    }

}
