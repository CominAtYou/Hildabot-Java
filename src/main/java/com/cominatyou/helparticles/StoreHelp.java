package com.cominatyou.helparticles;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

public class StoreHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("Store")
        .setDescription("View and purchase items with tokens. The store can only be invoked via the </store:1071965429012107486> command.")
        .addField("Examples", "• </store:1071965429012107486>")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
