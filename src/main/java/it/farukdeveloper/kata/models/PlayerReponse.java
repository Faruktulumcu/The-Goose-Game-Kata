package it.farukdeveloper.kata.models;

import java.util.Objects;

/**
 * @author Faruk Tulumcu
 */
public class PlayerReponse {

    private int targetPosition;

    private String finalMessage;

    public PlayerReponse() {
        this.finalMessage = "";
    }

    public PlayerReponse(int targetPosition, String finalMessage) {
        this.targetPosition = targetPosition;
        this.finalMessage = finalMessage;
    }

    public PlayerReponse(String finalMessage) {
        this.finalMessage = finalMessage;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    public void addToTargetPosition(int targetPosition) {
        if (Objects.nonNull(this.targetPosition)) {
            this.targetPosition += targetPosition;
        } else {
            this.targetPosition = targetPosition;
        }
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
    }

    public void concatToFinalMessage(String message) {
        this.finalMessage += finalMessage;
    }

}
