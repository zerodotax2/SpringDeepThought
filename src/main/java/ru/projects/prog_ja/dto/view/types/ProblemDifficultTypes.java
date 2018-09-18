package ru.projects.prog_ja.dto.view.types;

public enum ProblemDifficultTypes {

    HELL, HARD, NORMAL, EASY, UNKNOWN;

    public ProblemDifficultTypes fromString(String type){
        switch (type){
            case "hell":
                return HELL;
            case "hard":
                return HARD;
            case "normal":
                return NORMAL;
            case "easy":
                return EASY;
            default:
                return EASY;
        }
    }
}
