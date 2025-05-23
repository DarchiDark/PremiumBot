package org.avangard.util;

public class StringUtil {
    public static int percent(String typed, String origin, boolean ignoreCase) {
        if (ignoreCase) {
            typed = typed.toLowerCase();
            origin = origin.toLowerCase();
        }
        if (typed.equals(origin)) {
            return 100;
        }

        int length = Math.min(typed.length(), origin.length());
        int coincidences = 0;

        for (int i = 0; i < length; i++) {
            if (typed.charAt(i) == origin.charAt(i)) {
                coincidences++;
            }
        }

        return (int) ((coincidences / (double) origin.length()) * 100);
    }

}
