package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class SubmitHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("Submit")
        .setDescription("Submit content in <#492580926111481859>, <#492580873628286976>, <#492578733442465804>, <#492579714674720778>, or <#492885164993675264> to get XP! You can only submit once per day.")
        .addField("Parameters", "`file || url` - The content you want to submit, as a file attachment or URL.")
        .addField("Examples", "• h!submit https://www.youtube.com/watch?v=m8IRu3lEUPs\n• h!submit (only works when a file is attached to the message)")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
