package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class LevelCheckHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("LevelCheck")
        .setDescription("Re-check your roles and assign any that you're missing if the bot fails to assign you a role when you level up.")
        .addField("Examples", "• h!levelcheck")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
