package com.cominatyou;

import java.util.Map;
import static java.util.Map.entry;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.slashcommands.*;


public class SlashCommandHandler {
    private static final Map<String, InteractionCommand> commands = Map.ofEntries(
        entry("tagline", new Tagline()),
        entry("birthday", new Birthdays())
    );

    public static void route(SlashCommandCreateEvent interaction) {
        final SlashCommandInteraction slashCommand = interaction.getInteraction().asSlashCommandInteraction().get();
        final String commandName = slashCommand.getCommandName();
        if (!commands.containsKey(commandName)) return;

        commands.get(commandName).execute(slashCommand);
    }
}
