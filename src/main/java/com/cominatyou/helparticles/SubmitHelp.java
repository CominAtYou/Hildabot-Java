package com.cominatyou.helparticles;

import com.cominatyou.util.Values;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class SubmitHelp {
    public static void sendHelpArticle(MessageCreateEvent message) {
        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Submit command")
            .setColor(new java.awt.Color(Values.HILDA_BLUE))
            .setDescription("This command lets submit content (images, video, audio) to select channels to gain XP. It can be a great way to help you level up!")
            .addField("Submission Channels", "<#492579714674720778>, <#492578733442465804>, <#492885164993675264>, <#492580873628286976>, and <#492580926111481859>")
            .addField("Usage", "h!submit url|file attachment")
            .addField("Streaks", "Every time you submit, your streak will increase. Having a higher streak will help you gain more XP every time you submit. Your streak will be reset if you don't submit within a week of your latest submission.")
            .addField("Requirements", "- You can only submit once per day.\n- All submissions must comply with Discord ToS and Hildacord rules.");

        message.getMessage().reply("Sent you a DM with more info!");
        message.getMessageAuthor().asUser().get().openPrivateChannel().join().sendMessage(embed);
    }
}
