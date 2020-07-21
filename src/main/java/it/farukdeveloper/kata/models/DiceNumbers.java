package it.farukdeveloper.kata.models;

/**
 * @author Faruk Tulumcu
 */
public class DiceNumbers {

    private int dice1;

    private int dice2;

    public DiceNumbers() {
    }

    public DiceNumbers(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

}
