package com.cominatyou.helparticles;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

public class TimeLeftHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("TimeLeft")
        .setDescription("Get the time left for the current submission period. This command will also let you know when the next submission period is, in your time zone.")
        .addField("Examples", "• h!timeleft")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
