package com.cominatyou.commands;

import com.cominatyou.RedisUserEntry;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.util.Values;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Stats {
    public static void execute(MessageCreateEvent message) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        final int currentLevel = user.getLevel();
        final int currentStreak = RedisInstance.getInt("users:" + user.getID() + ":streak");
        final int xpForLevelUp =  XPSystemCalculator.determineMinimumUserFacingXPForLevel(currentLevel + 1);
        final String highScore = String.valueOf(RedisInstance.getInt("users:" + user.getID() + ":highscore"));
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
            .setColor(new java.awt.Color(Values.CODE_BLUE))
            .setDescription(String.format("%s, Level %d", RankUtil.getRankName(user.getLevel()), currentLevel))
            .addField("Progress:", progressCircles.toString())
            .addInlineField("XP:", user.getXP() - XPSystemCalculator.determineMinimumTotalXPForLevel(currentLevel) + "/" + xpForLevelUp)
            .addInlineField("Streak:", String.valueOf(currentStreak))
            .addInlineField("High Score:", highScore)
            .addField("Stats", "**Submits:** " + RedisInstance.getInt(user.getRedisKey() + ":timessubmitted"))
            .addField("Submit Status", RedisInstance.getBoolean(user.getRedisKey() + ":submitted") ? ":white_check_mark: You have submitted today!" : "You have not submitted today.");

        final String streakExpiry = RedisInstance.getInstance().get("users:" + user.getID() + ":streakexpiry");
        if (streakExpiry != null) {
            embed.addField("Streak Expires", "<t:" + streakExpiry + ">");
        }
        message.getChannel().sendMessage(embed);
    }
}
