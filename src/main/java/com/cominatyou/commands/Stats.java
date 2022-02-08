package com.cominatyou.commands;

import java.awt.Color;
import java.util.Optional;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Stats {
    public static void execute(MessageCreateEvent message) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());

        final int currentLevel = user.getLevel();
        final int currentStreak = user.getInt("streak");

        final int xpForLevelUp =  XPSystemCalculator.determineMinimumUserFacingXPForLevel(currentLevel + 1);
        final String highScore = String.valueOf(user.getInt("highscore"));

        final Optional<Color> roleColor = message.getServer().get().getRoleColor(message.getMessageAuthor().asUser().get());

        final int numberOfFilledInCircles = Math.round((float) (user.getXP() - (float) XPSystemCalculator.determineMinimumTotalXPForLevel(currentLevel)) / (float) xpForLevelUp * 20f);
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
            .setColor(roleColor.orElse(new Color(Values.HILDA_BLUE)))
            .setDescription(String.format("%s, Level %d", RankUtil.getRankNameFromLevel(user.getLevel()), currentLevel))
            .addField("Progress", progressCircles.toString())
            .addInlineField("XP", user.getXP() - XPSystemCalculator.determineMinimumTotalXPForLevel(currentLevel) + "/" + xpForLevelUp)
            .addInlineField("Streak", String.valueOf(currentStreak))
            .addInlineField("High Score", highScore)
            .addField("Stats", "**Submits:** " + user.getInt("timessubmitted"))
            .addField("Submit Status", user.getBoolean("submitted") ? ":white_check_mark: You have submitted today!" : "You have not submitted today.");

        final String streakExpiry = user.getString("streakexpiry");
        if (streakExpiry != null) {
            embed.addField("Streak Expiry", "<t:" + streakExpiry + ">");
        }

        message.getChannel().sendMessage(embed);
    }
}
