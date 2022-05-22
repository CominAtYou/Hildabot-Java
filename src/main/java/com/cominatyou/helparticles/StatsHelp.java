package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class StatsHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("Stats")
        .setDescription("Get your stats, or the stats of another user.")
        .addField("Parameters", "• `id` - The user ID of the member who you want to get the stats of. Omit this to get your own stats.")
        .addField("Examples", "• h!stats\n• h!stats 245047280908894209")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
