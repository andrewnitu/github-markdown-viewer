package com.andrewnitu.githubmarkdownviewer.utility;

/**
 * Created by Andrew Nitu on 7/31/2017.
 */

public class ExtractIntFromEnd {

    /**
     *
     * @param   string  the string to be parsed
     * @return          the integer found at the end of the given string, including negatives
     */
    public static int extractEndInt(String string) {
        if (string.length() == 0) {
            return -1;
        }
        int index = string.length() - 1;

        while (index >= 0 && (Character.isDigit(string.charAt(index)) || string.charAt(index) == '-')) {
            index--;
        }

        return Integer.valueOf(string.substring(index + 1));
    }
}
