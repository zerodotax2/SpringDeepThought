package ru.projects.prog_ja.dto.view.types;

public enum UserViewTypes {

    RATING, NEW, MODER, UNKNOWN;

    public UserViewTypes fromString(String type){
        switch (type){
            case "rating":
                return RATING;
            case "NEW":
                return NEW;
            case "MODER":
                return MODER;

            default:
                return RATING;
        }
    }
}
