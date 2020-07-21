package it.farukdeveloper.kata.utils;

import java.util.Objects;

/**
 * @author Faruk Tulumcu
 */
public class Utils {

    public static String clearCaseInsensitive(String from, String what) {
        if (Objects.nonNull(from) && Objects.nonNull(what)) {
            return from.replaceAll("(?i)" + what, "");
        }
        return from;
    }
}
