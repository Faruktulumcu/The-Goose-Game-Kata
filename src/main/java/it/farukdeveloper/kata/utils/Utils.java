package it.farukdeveloper.kata.utils;

import java.util.Objects;

/**
 * @author Faruk Tulumcu
 */
public class Utils {

    /**
     * revemos all occurrence of what case insensitive
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
}
