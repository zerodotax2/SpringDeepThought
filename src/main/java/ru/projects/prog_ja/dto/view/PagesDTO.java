package ru.projects.prog_ja.dto.view;

public class PagesDTO {

    private final int first;
    private final int last;
    private final int current;
    private final int total;

    public PagesDTO(int first, int last, int current, int total) {
        this.first = first;
        this.last = last;
        this.current = current;
        this.total = total;
    }

    public int getFirst() {
        return this.first;
    }

    public int getLast() {
        return this.last;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getTotal() {
        return this.total;
    }
}
