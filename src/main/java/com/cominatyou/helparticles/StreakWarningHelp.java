package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class StreakWarningHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("StreakWarning")
        .setDescription("Configure whether or not you want the bot to DM you when your streak is about to expire. Using this command will toggle warnings on or off.")
        .addField("Examples", "• h!streakwarning")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
