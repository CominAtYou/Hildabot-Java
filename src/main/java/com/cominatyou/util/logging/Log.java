package com.cominatyou.util.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.cominatyou.App;
import com.cominatyou.util.Values;

public class Log {
    /**
     * Log a timestampped event to the console and the {@link Values#ERROR_RED log channel}.
     * @param event The name of the event.
     * @param info Information to be printed out with the log.
     */
    public static void event(String event, String info) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now();
        final String timeString = timeFormat.format(currentTime);

        System.out.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), info);

        App.getClient().getChannelById(Values.LOG_CHANNEL).ifPresent(channel -> {
            channel.asServerTextChannel().get().sendMessage(String.format("[%s] %s", event.toUpperCase(), info));
        });
    }

    /**
     * Log a formatted timestampped event to the console and the {@link Values#ERROR_RED log channel}.
     * @param event The name of the event.
     * @param fString A {@code printf} format string.
     * @param args Format arguments for {@code fString}.
     */
    public static void eventf(String event, String format, Object... args) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final String timeString = timeFormat.format(currentTime);

        final String info = String.format(format, args);
        System.out.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), info);

        App.getClient().getChannelById(Values.LOG_CHANNEL).ifPresent(channel -> {
            channel.asServerTextChannel().get().sendMessage(String.format("[%s] %s", event.toUpperCase(), info));
        });
    }

    /**
     * Log a timestamped error to the console and the {@link Values#ERROR_RED log channel}.
     * @param event The name of the event.
     * @param error The error details.
     */
    public static void error(String event, String error) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final String timeString = timeFormat.format(currentTime);

        System.err.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), error);

        App.getClient().getChannelById(Values.LOG_CHANNEL).ifPresent(channel -> {
            final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Error")
                .setColor(Values.ERROR_RED)
                .addField("Event", event)
                .addField("Message", error);

            channel.asServerTextChannel().get().sendMessage(embed);
        });
    }

    /**
     * Log a timestamped error to the console and the {@link Values#ERROR_RED log channel}.
     * @param event The name of the event.
     * @param error The error details.
     * @param extendedMessage An extended message (such as a stack trace) to be sent to the log channel.
     */
    public static void error(String event, String error, String extendedMessage) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final String timeString = timeFormat.format(currentTime);

        System.err.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), error);

        App.getClient().getChannelById(Values.LOG_CHANNEL).ifPresent(channel -> {
            channel.asServerTextChannel().get().sendMessage(String.format("[%s ERROR] %s\n```%s```", event.toUpperCase(), error, extendedMessage));
        });
    }

    /**
     * Log a formatted timestamped error to the console and the {@link Values#ERROR_RED log channel}.
     * @param event The name of the event.
     * @param fString A {@code printf} format string.
     * @param args Format arguments for {@code fString}.
     */
    public static void errorf(String event, String format, Object... args) {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime currentTime = LocalDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final String timeString = timeFormat.format(currentTime);

        final String error = String.format(format, args);
        System.err.printf("%s -- [%s] %s\n", timeString, event.toUpperCase(), error);

        App.getClient().getChannelById(Values.LOG_CHANNEL).ifPresent(channel -> {
            final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Error")
                .setColor(Values.ERROR_RED)
                .addField("Event", event)
                .addField("Message", error);

            channel.asServerTextChannel().get().sendMessage(embed);
        });
    }
}
