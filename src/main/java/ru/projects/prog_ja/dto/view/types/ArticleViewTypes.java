package ru.projects.prog_ja.dto.view.types;

public enum ArticleViewTypes {
    RATING, NEW, UNKNOWN;

    public ArticleViewTypes fromString(String type){
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
