package com.cominatyou.slashcommands;

import org.javacord.api.interaction.SlashCommandInteraction;

public interface SlashCommand {
    public void execute(SlashCommandInteraction interaction);
}
