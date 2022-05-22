package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class LevelAlertHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("LevelAlert")
        .setDescription("Configure whether or not you want the bot to DM you when you level up. Using this command will toggle level alerts on or off.")
        .addField("Examples", "• h!levelalert")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
