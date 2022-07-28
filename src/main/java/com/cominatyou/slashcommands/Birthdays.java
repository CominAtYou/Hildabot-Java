package com.cominatyou.slashcommands;

import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.slashcommands.birthdays.EditBirthday;
import com.cominatyou.slashcommands.birthdays.ListBirthdays;
import com.cominatyou.slashcommands.birthdays.SetBirthday;

public class Birthdays implements InteractionCommand {
    public void execute(SlashCommandInteraction interaction) {
        switch (interaction.getOptionByIndex(0).get().getName()) {
            case "set": {
                SetBirthday.set(interaction);
                break;
            }
            case "edit": {
                EditBirthday.edit(interaction);
                break;
            }
            case "list": {
                ListBirthdays.list(interaction);
                break;
            }
        }
    }
}
