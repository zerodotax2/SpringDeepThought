package ru.projects.prog_ja.dto.view;

public enum ProblemDifficult {

    HELL("HELL"), HARD("HARD"), NORMAL("NORMAL"), EASY("EASY"), UNKNOWN("UNKNOWN");

    private final String name;

    ProblemDifficult(String str){
        this.name = str;
    }

    @Override
    public String toString(){
        return name;
    }

}
