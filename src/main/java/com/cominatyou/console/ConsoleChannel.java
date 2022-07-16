package com.cominatyou.console;

import org.javacord.api.entity.channel.ServerTextChannel;

public class ConsoleChannel {
    /**
     * The current channel the set is linked to.
     */
    private static ServerTextChannel channel = null;

    /**
     * Check if the console has been set to a channel.
     * @return {@code true} if the console has been set to a channel, {@code false} otherwise.
     */
    public static boolean isChannelSet() {
        return channel != null;
    }

    /**
     * Get the current channel the console is set to.
     * @return The current channel.
     */
    public static ServerTextChannel getCurrentChannel() {
        return channel;
    }

    /**
     * Set the channel the console should be set to.
     * @param channel The new channel the console should be set to.
     */
    public static void setChannel(ServerTextChannel channel) {
        ConsoleChannel.channel = channel;
    }
}
