package com.cominatyou.helparticles;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class AvailableHelpArticles {

    private static final String helpArticlesList = new StringBuilder()
        .append("• **birthday** - Add your birthday to the bot to get it announced on your birthday!\n")
        .append("• **levelalert** - Configure whether or not you want the bot to DM you on level-up.\n")
        .append("• **levelcheck** - Didn't get a role that you should have? This'll fix that.\n")
        .append("• **stats** - Get XP-related stats for you or another user.\n")
        .append("• **streakwarning** - Like levelalert, but for streak warnings.\n")
        .append("• **submit** - Submit content in certain channels to get some XP!").toString();

    public static final EmbedBuilder HELP_ARTICLES_EMBED = new EmbedBuilder()
        .setTitle("Help")
        .setDescription("Need help with a command? Send `h!help article_name` to get help for a specific command.")
        .addField("Available commands & articles", helpArticlesList)
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
