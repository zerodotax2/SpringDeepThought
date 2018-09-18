package ru.projects.prog_ja.dto.view.types;

public enum QuestionViewTypes {

    RATING, NEW, UNKNOWN;

    public QuestionViewTypes fromString(String type){
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
