package ru.projects.prog_ja.dto.smalls;

/**
 * Popular tag transfer object to show in top line
 * */
public class SmallTagTransfer implements Comparable<SmallTagTransfer>{

    protected long id;
    protected String name;
    protected String color;

    public SmallTagTransfer(long id, String name,String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Override
    public int compareTo(SmallTagTransfer secondTag){

        return Long.compare(getId(), secondTag.getId());
    }

    @Override
    public String toString(){
        return "ID: " +id+", Имя: " + name +", hex: " + color +";\n";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor(){
        return this.color;
    }

    public void setColor(String color){
        this.color = color;
    }
}
