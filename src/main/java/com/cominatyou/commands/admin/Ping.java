package com.cominatyou.commands.admin;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.commands.interfaces.Command;

public class Ping implements Command {
    public void execute(MessageCreateEvent message, List<String> args) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        final long latency = Duration.between(message.getMessage().getCreationTimestamp(), Instant.now()).toMillis();
        final long apiLatency = message.getApi().getLatestGatewayLatency().toMillis();

        message.getChannel().sendMessage(String.format(":ping_pong: Pong!\n\n**Bot latency:** %dms\n**Gateway latency:** %dms", latency, apiLatency));
    }

    public String getName() {
        return "Ping";
    }
}
