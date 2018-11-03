package ru.projects.prog_ja.dto.view.update;


import javax.validation.constraints.NotNull;

public class UpdateRatingDTO {

    @NotNull
    private long id;
    @NotNull
    private int rate;

    public UpdateRatingDTO() {
    }

    public UpdateRatingDTO(long id, int rate) {
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
