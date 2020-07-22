package it.farukdeveloper.kata;

import it.farukdeveloper.kata.models.DiceNumbers;
import it.farukdeveloper.kata.models.Player;
import it.farukdeveloper.kata.models.PlayerReponse;
import it.farukdeveloper.kata.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Goose Game Kata thought only for Java SE and Console platform
 *
 * @author Faruk Tulumcu
 */
public class Application {

    // created a global and shared player list in order to not reference in all methods
    private static List<Player> players;

    private static boolean doesGameStarted = false;
    private static boolean doesGameFinished = false;
    private final static String EXIT_COMMAND = "exit";
    private final static String ADD_PLAYER_COMMAND = "add player";
    private final static String MOVE_PLAYER_COMMAND = "move";
    private final static String MISUNDERSTAND_COMMAND = "i did not understand";
    private final static String START_GAME_COMMAND = "start game";
    private final static int WIN_POSITION = 63;
    private final static int BOUNCE_POSITION = 61;
    private final static int BRIDGE_POSITION = 6;
    private final static int BRIDGE_JUMP_TO_POSITION = 12;
    private final static List<Integer> GOOSE_POSITIONS = new ArrayList<Integer>(Arrays.asList(5, 9, 14, 18, 23, 27));

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        players = new ArrayList<>();

        System.out.println("Welcome to ### The Goose Game Kata ### ");

        String userInput = "";
        String command = "";
        PlayerReponse playerReponse;

        while (!command.equals(EXIT_COMMAND) && !doesGameFinished) {
            userInput = System.console().readLine();
            command = decodeCommand(userInput);
            switch (command) {
                case ADD_PLAYER_COMMAND:
                    playerReponse = handleAddPlayerCommand(userInput);
                    break;
                case MOVE_PLAYER_COMMAND:
                    playerReponse = handleMovePlayerCommand(userInput);
                    break;
                case START_GAME_COMMAND:
                    playerReponse = handleStartGameCommand();
                    break;
                case EXIT_COMMAND:
                    playerReponse = handleExitCommand();
                    break;
                default:
                    playerReponse = handleMisunderstandPlayerCommand();
                    break;
            }
            System.out.println(playerReponse.getFinalMessage());
        }

        System.out.println("\n\nThank you for enjoying with this game \nSee you! :) ");

    }

    /**
     * decodes command from user input, the method of decode is FIV (First
     * Inserted Valid)
     *
     * @param userInput
     * @return
     */
    private static String decodeCommand(String userInput) {
        userInput = userInput.toLowerCase();
        if (userInput.startsWith(ADD_PLAYER_COMMAND)) {
            return ADD_PLAYER_COMMAND;
        }

        if (userInput.startsWith(MOVE_PLAYER_COMMAND)) {
            return MOVE_PLAYER_COMMAND;
        }

        if (userInput.startsWith(EXIT_COMMAND)) {
            return EXIT_COMMAND;
        }

        if (userInput.startsWith(START_GAME_COMMAND)) {
            return START_GAME_COMMAND;
        }

        return MISUNDERSTAND_COMMAND;
    }

    /**
     * inserts new player to start of kata
     *
     * @param command
     * @return
     */
    private static PlayerReponse handleAddPlayerCommand(String command) {
        // if game started user cannot add another player until it ends
        if (doesGameStarted) {
            return new PlayerReponse("Game already started, you cannot add new player until game ends!");
        }

        command = Utils.clearCaseInsensitive(command, ADD_PLAYER_COMMAND);
        String playerName = command.trim();
        if (playerName.isEmpty()) {
            return new PlayerReponse("Please specify a valid player name, the right syntax is like below:\nAdd Player Mario");
        }

        if (playerName.toLowerCase().equals(ADD_PLAYER_COMMAND) || playerName.toLowerCase().equals(MOVE_PLAYER_COMMAND)) {
            return new PlayerReponse("Please specify a valid player name, player name cannot be keyword of command like (move, add player)");
        }

        // checks if there is an existing player with inserted name (case insensitive)
        Player foundPlayer = findPlayerByName(playerName);
        if (Objects.nonNull(foundPlayer)) {
            return new PlayerReponse(foundPlayer.getName() + ": already existing player");
        } else {
            foundPlayer = new Player(playerName);
            players.add(foundPlayer);
            String allPlayersName = String.join(", ", players.stream().map(p -> p.getName()).collect(Collectors.toList()));
            return new PlayerReponse("Players: " + allPlayersName);
        }
    }

    private static PlayerReponse handleMovePlayerCommand(String command) {
        PlayerReponse moveInfo = new PlayerReponse();
        if (!doesGameStarted) {
            moveInfo.setFinalMessage("Game not started, please enter \"start game\" in order to start.");
            return moveInfo;
        }
        command = Utils.clearCaseInsensitive(command, MOVE_PLAYER_COMMAND);
        command = command.trim();
        //clear also player name

        // find which player to move
        Player extractedPlayer = extractPlayer(command);
        if (Objects.isNull(extractedPlayer)) {
            moveInfo.setFinalMessage("I do not understand which player move, please enter a correct syntax like below: \nMove Mario 4, 2 \nMove Mario");
            return moveInfo;
        }

        // remove also player name from user command
        command = Utils.clearCaseInsensitive(command, extractedPlayer.getName());

        DiceNumbers diceNumbers;

        // If there aren't dice info -> the dice info will added by system -> 4. The game throws the dice 
        if (command.trim().isEmpty()) {
            diceNumbers = new DiceNumbers(rollTheDice(), rollTheDice());
        } else {
            diceNumbers = extractDiceNumbers(command, moveInfo);
        }

        if (Objects.isNull(diceNumbers)) {
            moveInfo.concatToFinalMessage("Invalid numbers entered, please enter a correct syntax like below: \nMove Mario 4, 2 \nMove Mario");
            return moveInfo;
        }

        moveInfo = rollPlayer(extractedPlayer, diceNumbers, moveInfo);

        return moveInfo;
    }

    private static PlayerReponse rollPlayer(Player player, DiceNumbers diceNumbers, PlayerReponse moveInfo) {
        int curretPlayerPreviousPosition = player.getPosition();
        int pIdx = players.indexOf(player);
        Integer targetPosition = player.getPosition() + diceNumbers.getDice1() + diceNumbers.getDice2();

        String rollMessage = player.getName() + " rolls " + diceNumbers.getDice1() + ", " + diceNumbers.getDice2() + ". ";
        String moveMessage = player.getName() + " moves from " + decodeFromPositionString(player.getPosition()) + " to " + decodeTargetPositionString(targetPosition);
        String finalMessage = rollMessage + moveMessage;

        if (targetPosition == BRIDGE_POSITION) {
            targetPosition = BRIDGE_JUMP_TO_POSITION;
            String bridgeMessage = " " + player.getName() + " jumps to " + BRIDGE_JUMP_TO_POSITION;
            finalMessage += bridgeMessage;
        }

        if (targetPosition == WIN_POSITION) {
            String winMessage = " " + player.getName() + " Wins!!";
            finalMessage += winMessage;
            doesGameFinished = true;
        }

        if (targetPosition > WIN_POSITION) {
            String bounceMessage = " " + player.getName() + " bounces! " + player.getName() + " returns to " + BOUNCE_POSITION;
            finalMessage += bounceMessage;
            targetPosition = BOUNCE_POSITION;
        }

        moveInfo = new PlayerReponse(targetPosition, finalMessage);

        // works by reference
        checkAndHandleGoosePosition(moveInfo, diceNumbers, player); // TODO enhance it

        player.setPosition(moveInfo.getTargetPosition());
        players.set(pIdx, player);

        //
        moveInfo = checkAndHandleForPrank(player, moveInfo, curretPlayerPreviousPosition);

        return moveInfo;
    }

    /**
     * adds prank message to moveInfo object sets pranked users position
     * @param currentPlayer
     * @param moveInfo
     * @param curretPlayerPreviousPosition
     */
    private static PlayerReponse checkAndHandleForPrank(Player currentPlayer, PlayerReponse moveInfo, int curretPlayerPreviousPosition) {
        for (Player p : players) {
            if (!p.getName().equals(currentPlayer.getName()) && p.getPosition() == currentPlayer.getPosition()) {
                String prankMessage = " On " + decodeFromPositionString(p.getPosition()) + " there is " + p.getName() + ", who returns to " + decodeFromPositionString(curretPlayerPreviousPosition);
                int pIdx = players.indexOf(p);
                p.setPosition(curretPlayerPreviousPosition);
                players.set(pIdx, p);
                moveInfo.concatToFinalMessage(prankMessage);
                return moveInfo;
            }
        }
        return moveInfo;
    }


    /**
     * handle goose position with moveInfo reference
     * @return
     */
    private static void checkAndHandleGoosePosition(PlayerReponse moveInfo, DiceNumbers diceNumbers, Player p) {
        if (GOOSE_POSITIONS.indexOf(moveInfo.getTargetPosition()) != -1) {
            moveInfo.addToTargetPosition(diceNumbers.getDice1() + diceNumbers.getDice2());
            String gooseMessage = ", The Goose." + p.getName() + " moves again and goes to " + moveInfo.getTargetPosition();
            moveInfo.concatToFinalMessage(gooseMessage);
            checkAndHandleGoosePosition(moveInfo, diceNumbers, p); // checks recursively
        }
    }

    /**
     * extracts dice numbers from user input command
     * @param command
     * @param moveInfo
     * @return 
     */
    private static DiceNumbers extractDiceNumbers(String command, PlayerReponse moveInfo) {
        String[] splittedNumbers = command.split(",");
        if (splittedNumbers.length == 2) {
            try {
                int dice1 = Integer.valueOf(splittedNumbers[0].trim());
                int dice2 = Integer.valueOf(splittedNumbers[1].trim());
                if (dice1 < 1 || dice1 > 6 || dice2 < 1 || dice2 > 6) {
                    moveInfo.concatToFinalMessage("Error: you entered an invalid dice number ![1-6] ");
                    return null;
                }
                return new DiceNumbers(dice1, dice2);
            } catch (NumberFormatException e) {
                moveInfo.concatToFinalMessage("Error invalid number: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * extract a registered/added player from user input command
     * @param command
     * @return 
     */
    private static Player extractPlayer(String command) {
        // example of player names present: Mario Rossi, Marco Bianchi, Mario Rossana
        // pippo, pippooo || move pippo 2,3 || move pippo (without end space)
        for (Player p : players) {
            if (command.toLowerCase().startsWith(p.getName().toLowerCase())
                    && command.toLowerCase().contains(p.getName().toLowerCase() + " ")
                    || (command.toLowerCase().startsWith(p.getName().toLowerCase()) && command.toLowerCase().endsWith(p.getName().toLowerCase()))) {
                return p;
            }
        }
        return null;
    }

    /**
     * handles start game command
     * @return 
     */
    private static PlayerReponse handleStartGameCommand() {
        if (doesGameStarted) {
            return new PlayerReponse("Game already started!");
        }

        // if there are less than 2 players game cannot start
        if (players.size() < 2) {
            return new PlayerReponse("There are not enough players, it is necessary almost 2 players, plase insert player.");
        }

        doesGameStarted = true;
        return new PlayerReponse("Game started!");
    }

    /**
     * handles misunderstand command
     * @return 
     */
    private static PlayerReponse handleMisunderstandPlayerCommand() {
        return new PlayerReponse("Sorry I don't understant what you wrote, please enter a valid/known command");
    }

    /**
     * handles exit user command
     * @return 
     */
    private static PlayerReponse handleExitCommand() {
        return new PlayerReponse("Thank you for playing");
    }
    
    
    /**
     * PRIVATE UTILITIES
    */
    
    /**
     * decodes target position
     * @param targetPosition
     * @return 
     */
    private static String decodeTargetPositionString(int targetPosition) {
        if (targetPosition == BRIDGE_POSITION) {
            return "The Bridge.";
        }
        return (targetPosition > WIN_POSITION ? String.valueOf(WIN_POSITION) : String.valueOf(targetPosition));
    }
    
    /**
     * decode from position
     * @param fromPosition
     * @return 
     */
    private static String decodeFromPositionString(int fromPosition){
        return fromPosition == 0 ? "Start" : String.valueOf(fromPosition);
    }

    /**
     * returns a random number from 1 to 6
     * @return 
     */
    private static int rollTheDice() {
        return (int) (Math.random() * 6 + 1);
    }

    /**
     * find player by name case insensitive
     * @param enteredPlayerName
     * @return
     */
    private static Player findPlayerByName(String enteredPlayerName) {
        for (Player p : players) {
            if (p.getName().toLowerCase().equals(enteredPlayerName.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

}
