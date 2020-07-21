package it.farukdeveloper.kata;

import it.farukdeveloper.kata.models.DiceNumbers;
import it.farukdeveloper.kata.models.Player;
import it.farukdeveloper.kata.models.PlayerMoveInfo;
import it.farukdeveloper.kata.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Faruk Tulumcu
 */
public class Application {

    private static List<Player> players;
    private static boolean doesGameStarted = false;
    private final static String EXIT_COMMAND = "end";
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

        String line = "";
        String command = "";

        while (!command.equals(EXIT_COMMAND)) {
            line = System.console().readLine();
            command = decodeCommand(line);
            switch (command) {
                case ADD_PLAYER_COMMAND:
                    handleAddPlayerCommand(line);
                    break;
                case MOVE_PLAYER_COMMAND:
                    handleMovePlayerCommand(line);
                    break;
                case START_GAME_COMMAND:
                    handleStartGameCommand();
                    break;
                case EXIT_COMMAND:
                    handleExitCommand(line);
                    break;
                case MISUNDERSTAND_COMMAND:
                    handleMisunderstandPlayerCommand(line);
                    break;
                default:
                    handleMisunderstandPlayerCommand(line);
                    break;
            }
        }

        System.out.println("Thank you for playing ### The Goose Game Kata ### \n See you! :) ");

    }

    /**
     * decodes command from user input, the method of decode is FIV (First
     * Inserted Valid)
     *
     * @param line
     * @return
     */
    private static String decodeCommand(String line) {
        line = line.toLowerCase();
        if (line.startsWith(ADD_PLAYER_COMMAND)) {
            return ADD_PLAYER_COMMAND;
        }

        if (line.startsWith(MOVE_PLAYER_COMMAND)) {
            return MOVE_PLAYER_COMMAND;
        }

        if (line.startsWith(EXIT_COMMAND)) {
            return EXIT_COMMAND;
        }

        if (line.startsWith(START_GAME_COMMAND)) {
            return START_GAME_COMMAND;
        }

        return MISUNDERSTAND_COMMAND;
    }

    /**
     * inserts new player to start of kata
     *
     * @param command
     */
    private static void handleAddPlayerCommand(String command) {

        // -> fix -> exclude keywords from players name, like move -> move move 4, 2
        // if game started user cannot add another player until it ends
        if (doesGameStarted) {
            System.out.println("Game already started, you cannot add new player until game ends!");
            return;
        }

        command = Utils.clearCaseInsensitive(command, ADD_PLAYER_COMMAND);
        String playerName = command.trim();
        if (playerName.isEmpty()) {
            System.out.println("Please specify a valid player name to insert in game, the right syntax is like below:\nAdd Player Mario");
            return;
        }

        // checks if there is an existing player with inserted name (case insensitive)
        Player foundPlayer = findPlayerByName(playerName);
        if (Objects.nonNull(foundPlayer)) {
            System.out.println(foundPlayer.getName() + ": already existing player");
        } else {
            foundPlayer = new Player(playerName);
            players.add(foundPlayer);
            String allPlayersName = String.join(", ", players.stream().map(p -> p.getName()).collect(Collectors.toList()));
            System.out.println("Players: " + allPlayersName);
        }
    }

    private static void handleMovePlayerCommand(String command) {

        if (!doesGameStarted) {
            System.err.println("Game not started, please enter <start game> in order to start.");
            return;
        }

        /*
        int d1 = rollTheDice();
        int d2 = rollTheDice();
        System.out.println("You wrote: handleMovePlayerCommand: I am rolling the dice " + d1 + ", " + d2);
         */
        command = Utils.clearCaseInsensitive(command, MOVE_PLAYER_COMMAND);
        command = command.trim();
        //clear also player name

        // find which player to move
        //move Pippo 4, 2
        Player extractedPlayer = extractPlayer(command);
        if (Objects.isNull(extractedPlayer)) {
            System.err.println("I do not understand which player move, please enter a correct syntax like below: \n Move Mario 4, 2");
            return;
        }

        command = Utils.clearCaseInsensitive(command, extractedPlayer.getName());

        DiceNumbers diceNumbers = extractDiceNumbers(command);

        if (Objects.isNull(diceNumbers)) {
            System.err.println("Invalid numbers entered, please enter a correct syntax like below: \n Move Mario 4, 2");
            return;
        }

        movePlayer(extractedPlayer, diceNumbers);

    }

    private static void movePlayer(Player p, DiceNumbers diceNumbers) {
        //Pippo rolls 4, 2. Pippo moves from Start to 6
        //"Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61"

        String playerCurrentPosition = p.getPosition() == 0 ? "Start" : String.valueOf(p.getPosition());
        int pIdx = players.indexOf(p);
        Integer targetPosition = p.getPosition() + diceNumbers.getDice1() + diceNumbers.getDice2();

        String rollMessage = p.getName() + " rolls " + diceNumbers.getDice1() + ", " + diceNumbers.getDice2() + ". ";
        String moveMessage = p.getName() + " moves from " + playerCurrentPosition + " to " + decodeTargetPositionString(targetPosition);
        String finalMessage = rollMessage + moveMessage;

        if (targetPosition == BRIDGE_POSITION) {
            targetPosition = BRIDGE_JUMP_TO_POSITION;
            String bridgeMessage = " " + p.getName() + " jumps to " + BRIDGE_JUMP_TO_POSITION;
            finalMessage += bridgeMessage;
        }

        if (targetPosition == WIN_POSITION) {
            String winMessage = " " + p.getName() + " Wins!!";
            finalMessage += winMessage;
        }

        if (targetPosition > WIN_POSITION) {
            String bounceMessage = " " + p.getName() + " bounces! " + p.getName() + " returns to " + BOUNCE_POSITION;
            finalMessage += bounceMessage;
            targetPosition = BOUNCE_POSITION;
        }

        //"Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22"
        PlayerMoveInfo moveInfo = new PlayerMoveInfo(targetPosition, finalMessage);

        handleGoosePosition(moveInfo, diceNumbers, p); // TODO enhance it

        p.setPosition(moveInfo.getTargetPosition());
        players.set(pIdx, p);

        System.out.println(moveInfo.getFinalMessage());

    }

    private static String decodeTargetPositionString(int targetPosition) {
        if (targetPosition == BRIDGE_POSITION) {
            return "The Bridge.";
        }
        return (targetPosition > WIN_POSITION ? "63" : String.valueOf(targetPosition));
    }

    /**
     * handle goose position with references
     *
     * @return
     */
    private static void handleGoosePosition(PlayerMoveInfo moveInfo, DiceNumbers diceNumbers, Player p) {
        if (GOOSE_POSITIONS.indexOf(moveInfo.getTargetPosition()) != -1) {
            moveInfo.setTargetPosition(moveInfo.getTargetPosition() + diceNumbers.getDice1() + diceNumbers.getDice2());
            String gooseMessage = ", The Goose." + p.getName() + " moves again and goes to " + moveInfo.getTargetPosition();
            moveInfo.setFinalMessage(moveInfo.getFinalMessage() + gooseMessage);
            handleGoosePosition(moveInfo, diceNumbers, p); // check recursively
        }
    }

    private static DiceNumbers extractDiceNumbers(String command) {
        String[] splittedNumbers = command.split(",");
        if (splittedNumbers.length == 2) {
            try {
                int dice1 = Integer.valueOf(splittedNumbers[0].trim());
                int dice2 = Integer.valueOf(splittedNumbers[1].trim());
                if (dice1 < 1 || dice1 > 6 || dice2 < 1 || dice2 > 6) {
                    System.err.println("Error: you entered an invalid dice number ![1-6] ");
                    return null;
                }
                return new DiceNumbers(dice1, dice2);
            } catch (NumberFormatException e) {
                System.err.println("Error invalid number: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    private static Player extractPlayer(String command) {
        // example of player names present: Mario Rossi, Marco Bianchi, Mario Rossana
        // pippo, pippooo
        // reason of why used starts and not contains
        for (Player p : players) {
            //if (command.toLowerCase().contains(p.getName().toLowerCase())) {
            if (command.toLowerCase().startsWith(p.getName().toLowerCase()) && command.toLowerCase().contains(p.getName().toLowerCase() + " ")) {
                return p;
            }
        }
        return null;
    }

    private static void handleStartGameCommand() {
        if (doesGameStarted) {
            System.out.println("Game already started!");
            return;
        }

        // if there are less than 2 players game cannot start
        if (players.size() < 2) {
            System.out.println("There are not enough players, it is necessary almost 2 players, plase insert player.");
            return;
        }

        doesGameStarted = true;
        System.out.println("Game started!");

    }

    private static void handleMisunderstandPlayerCommand(String command) {
        System.out.println("You wrote: handleMisunderstandPlayerCommand: " + command);
    }

    private static void handleExitCommand(String command) {
        System.out.println("You wrote: handleExitCommand: " + command);
    }

    private static int rollTheDice() {
        return (int) (Math.random() * 6 + 1);
    }

    /**
     * find player by name case insensitive
     *
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
