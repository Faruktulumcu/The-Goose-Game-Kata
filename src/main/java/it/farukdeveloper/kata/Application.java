package it.farukdeveloper.kata;

import it.farukdeveloper.kata.controllers.PlayerController;
import it.farukdeveloper.kata.models.PlayerReponse;
import it.farukdeveloper.kata.utils.Utils;

/**
 * The Goose Game Kata thought only for Java SE and Console platform
 *
 * @author Faruk Tulumcu
 */
public class Application {

    private PlayerController playerController;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Application app = new Application();
        app.showInstructions();
        app.startGame();

    }

    private void startGame() {
        playerController = new PlayerController();
        String userInput = "";
        String command = "";
        PlayerReponse playerReponse;

        while (!command.equals(Utils.EXIT_COMMAND) && !playerController.isDoesGameFinished()) {
            userInput = System.console().readLine();
            command = Utils.decodeCommand(userInput);
            switch (command) {
                case Utils.ADD_PLAYER_COMMAND:
                    playerReponse = playerController.handleAddPlayerCommand(userInput);
                    break;
                case Utils.MOVE_PLAYER_COMMAND:
                    playerReponse = playerController.handleMovePlayerCommand(userInput);
                    break;
                case Utils.START_GAME_COMMAND:
                    playerReponse = playerController.handleStartGameCommand();
                    break;
                case Utils.EXIT_COMMAND:
                    playerReponse = playerController.handleExitCommand();
                    break;
                default:
                    playerReponse = playerController.handleMisunderstandPlayerCommand();
                    break;
            }
            System.out.println(playerReponse.getFinalMessage());
        }

        System.out.println("\n\nThank you for enjoying with this game\nSee you! :) ");
    }

    /**
     * shows games instructions
     */
    private void showInstructions() {
        System.out.println("Welcome to ### The Goose Game Kata ### ");
        System.out.println("*************************************************************************************************************");
        System.out.println("POSSIBLE COMMANDS ARE: ");
        System.out.println("1) \"add player <name>\"           | adds player                               | -> ex: add player Mario");
        System.out.println("2) \"start game\"                  | starts game                               | -> ex: start game");
        System.out.println("3) \"move <playerName>\"           | moves player with system's dice numbers   | -> ex: move Mario");
        System.out.println("4) \"move <playerName> <X>, <Y>\"  | moves player from current position to x+y | -> ex: move Mario 2, 4");
        System.out.println("5) \"exit\"                        | closes from game                          | -> ex: exit");
        System.out.println("*************************************************************************************************************");
    }

}
