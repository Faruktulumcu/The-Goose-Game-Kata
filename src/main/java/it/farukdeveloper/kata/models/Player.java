package it.farukdeveloper.kata.models;

/**
 * @author Faruk Tulumcu
 */
public class Player {

    private String name;

    private int position;

    public Player() {
    }

    public Player(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public Player(String name) {
        this.name = name;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
