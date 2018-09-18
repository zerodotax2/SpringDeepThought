package ru.projects.prog_ja.dto.view.types;

public enum ProblemViewTypes {
    RATING, NEW,  UNKNOWN;

    public ProblemViewTypes fromString(String type){
        switch (type){
            case "rating":
                return RATING;
            case "new":
                return NEW;

            default:
                return RATING;
        }
    }
}
