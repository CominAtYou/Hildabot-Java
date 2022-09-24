package com.cominatyou.slashcommands.birthdays;

import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

public class EditBirthday {
    public static void edit(SlashCommandInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        final boolean hasBirthdaySet = user.hasKey("birthday:string");

        if (!hasBirthdaySet) {
            interaction.createImmediateResponder().setContent("You don't have a birthday set! If you're trying to set your birthday, please use </birthday set:1011153003853643816>.").respond();
            return;
        }

        final String month = user.getString("birthday:month");
        final String day = user.getString("birthday:day");

        RedisInstance.getInstance().lrem(String.format("birthdays:%s:%s", month, day), 0, user.getIdAsString());
        RedisInstance.getInstance().del("users:" + user.getId() + ":birthday:month", "users:" + user.getId() + ":birthday:day", "users:" + user.getId() + ":birthday:string");

        SetBirthday.set(interaction);
    }
}
