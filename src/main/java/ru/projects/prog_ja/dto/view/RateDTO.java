package ru.projects.prog_ja.dto.view;

public class RateDTO {

    private long id;
    private int rate;

    public RateDTO() {
    }

    public RateDTO(long id, int rate) {
        this.id = id;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
