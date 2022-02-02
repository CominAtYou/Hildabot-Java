package com.cominatyou.helparticles;

import com.cominatyou.util.Values;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class BirthdaysHelp {
    public static void sendHelpArticle(MessageCreateEvent message) {
        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Birthday command")
            .setColor(new java.awt.Color(Values.HILDA_BLUE))
            .setDescription("This command lets you set your birthday, or edit it if you've already set it.\nBirthdays will be announced the day of at 12:00 AM Central Daylight/Standard Time (<t:1537506000:t> in your time zone) in <#609253148564914187>.\n\nBirthdays on 29 Feburary will be announced on 1 March during non-leap years.")
            .addField("Syntax", "h!birthday edit|set mm-dd")
            .addField("'Set' Parameter", "Set your birthday, if it has not been set. A date must be provided in the mm-dd format. (i.e. 06-08 for 8 June)")
            .addField("'Edit' Parameter", "Identical to set, but lets you edit your birthday if it has already been set.");
        message.getMessage().reply("Sent you a DM with more info!");
        message.getMessageAuthor().asUser().get().openPrivateChannel().join().sendMessage(embed);
    }
}
