package it.farukdeveloper.kata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Faruk Tulumcu
 */
public class Utils {

    public final static String EXIT_COMMAND = "exit";
    public final static String ADD_PLAYER_COMMAND = "add player";
    public final static String MOVE_PLAYER_COMMAND = "move";
    public final static String MISUNDERSTAND_COMMAND = "i did not understand";
    public final static String START_GAME_COMMAND = "start game";
    public final static int WIN_POSITION = 63;
    public final static int BOUNCE_POSITION = 61;
    public final static int BRIDGE_POSITION = 6;
    public final static int BRIDGE_JUMP_TO_POSITION = 12;
    public final static List<Integer> GOOSE_POSITIONS = new ArrayList<Integer>(Arrays.asList(5, 9, 14, 18, 23, 27));

    /**
     * revemos all occurrence of what case insensitive
     *
     * @param from
     * @param what
     * @return
     */
    public static String clearCaseInsensitive(String from, String what) {
        if (Objects.nonNull(from) && Objects.nonNull(what)) {
            return from.replaceAll("(?i)" + what, "");
        }
        return from;
    }

    /**
     * decodes command from user input, the method of decode is FIV (First
     * Inserted Valid)
     *
     * @param userInput
     * @return
     */
    public static String decodeCommand(String userInput) {
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
}
