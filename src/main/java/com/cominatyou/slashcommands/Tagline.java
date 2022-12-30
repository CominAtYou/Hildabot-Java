package com.cominatyou.slashcommands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

public class Tagline implements SlashCommand {
    public void execute(SlashCommandInteraction interaction) {
        if (!RedisInstance.getInstance().lrange("config:tagline:allowedusers", 0, -1).contains(interaction.getUser().getIdAsString())) {
            interaction.createImmediateResponder().setContent("You're not allowed to use this!").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        final String subCommand = interaction.getOptionByIndex(0).get().getName();

        if (subCommand.equals("set")) set(interaction);
        else remove(interaction);
    }

    private void set(SlashCommandInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        final String newTagline = interaction.getOptionByIndex(0).get().getOptionStringValueByIndex(0).get();

        user.set("tagline", newTagline);
        interaction.createImmediateResponder().setContent("Your tagline has been updated.").respond();
    }

    private void remove(SlashCommandInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        user.deleteKey("tagline");
        interaction.createImmediateResponder().setContent("Your tagline has been removed.").respond();
    }
}
