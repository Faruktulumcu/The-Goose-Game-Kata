package it.farukdeveloper.kata.models;

/**
 * @author Faruk Tulumcu
 */
public class PlayerMoveInfo {

    private int targetPosition;

    private String finalMessage;

    public PlayerMoveInfo() {
    }

    public PlayerMoveInfo(int targetPosition, String finalMessage) {
        this.targetPosition = targetPosition;
        this.finalMessage = finalMessage;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
    }

}
