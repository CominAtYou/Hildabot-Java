package com.cominatyou.commands.admin;

import java.time.Duration;
import java.time.Instant;

import org.javacord.api.event.message.MessageCreateEvent;

public class Ping {
    public static void pong(MessageCreateEvent message) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        final long latency = Duration.between(message.getMessage().getCreationTimestamp(), Instant.now()).toMillis();
        final long apiLatency = message.getApi().getLatestGatewayLatency().toMillis();

        message.getChannel().sendMessage(String.format(":ping_pong: Pong!\n\n**Bot latency:** %dms\n**Gateway latency:** %dms", latency, apiLatency));
    }
}
