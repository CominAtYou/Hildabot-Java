package com.cominatyou.util.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    /**
     * Log a timestampped event to the console.
     * @param event The name of the event.
     * @param info Information to be printed out with the log.
     */
    public static void event(String event, String info) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now();
        final String timeString = timeFormat.format(currentTime);

        System.out.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), info);
    }

    /**
     * Log a formatted timestampped event to the console.
     * @param event The name of the event.
     * @param fString A {@code printf} format string.
     * @param args Format arguments for {@code fString}.
     */
    public static void eventf(String event, String fString, Object... args) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now();
        final String timeString = timeFormat.format(currentTime);

        final String info = String.format(fString, args);
        System.out.printf("%s -- [%s] %s", timeString, event, info);
    }
}
