package ru.projects.prog_ja.dto.view;

import java.util.List;

public class BySomethingContainer {

    private boolean more;
    private List arr;

    public BySomethingContainer() {
    }

    public BySomethingContainer(boolean more, List arr) {
        this.more = more;
        this.arr = arr;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List getArr() {
        return arr;
    }

    public void setArr(List arr) {
        this.arr = arr;
    }
}
