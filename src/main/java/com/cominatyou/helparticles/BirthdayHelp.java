package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class BirthdayHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setColor(Values.HILDA_BLUE)
        .setTitle("Birthdays")
        .setDescription("Allows you to register your birthday with the bot to have your birthday announced in <#609253148564914187>, as well as receiving a nifty role for the duration of your birthday!\n**Subcommands:**")
        .addField("Set", "Set your birthday to a certain day. Specify your birthday in `mm-dd` format, prepending any number with 0 if it is less than 10.")
        .addField("Edit", "Edit your birthday. The syntax is idential to the `set` subcommand.")
        .addField("List", "List birthdays for a certain month. Specify the month with the number representing the month, or omit it to get birthdays for this month.")
        .addField("Examples", "• h!birthday set 01-02\n• h!birthday edit 06-21\n• h!birthday list 3")
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
