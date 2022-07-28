package com.cominatyou.slashcommands;

import org.javacord.api.interaction.SlashCommandInteraction;

public interface InteractionCommand {
    public void execute(SlashCommandInteraction interaction);
}
