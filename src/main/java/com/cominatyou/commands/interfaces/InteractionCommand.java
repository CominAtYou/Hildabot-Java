package com.cominatyou.commands.interfaces;

import org.javacord.api.interaction.SlashCommandInteraction;

public interface InteractionCommand {
    public void execute(SlashCommandInteraction interaction);
}
