package com.cominatyou.commands;

import com.cominatyou.RedisUserEntry;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Stats {
    public static void execute(MessageCreateEvent message) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        final int currentLevel = user.getLevel();
        final int xpForLevelUp =  XPSystemCalculator.determineMinimumXPForLevel(currentLevel + 1);
        final int numberOfFilledInCircles = Math.round((float) user.getXP() / (float) xpForLevelUp * 20f);
        final StringBuilder progressCircles = new StringBuilder();

        for (int i = 0; i < numberOfFilledInCircles; i++) {
            progressCircles.append("●");
        }
        for (int i = 0; i < 20 - numberOfFilledInCircles; i++) {
            progressCircles.append("○");
        }

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(message.getMessageAuthor().getDisplayName())
            .setThumbnail(message.getMessageAuthor().getAvatar())
            .setColor(new java.awt.Color(0x007acc))
            .setDescription(String.format("%s, Level %d", RankUtil.getRankName(user.getLevel()), currentLevel))
            .addField("Progress", progressCircles.toString())
            .addInlineField("XP", user.getXP() + "/" + xpForLevelUp)
            .addInlineField("Streak", "69")
            .addInlineField("High Score:", "180")
            .addField("Stats", "**Submits:** 238")
            .addField("Submit Status", ":white_check_mark: You have submitted today!");
        message.getChannel().sendMessage(embed);
    }
}
