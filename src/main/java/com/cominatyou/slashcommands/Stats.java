package com.cominatyou.slashcommands;

import java.awt.Color;
import java.util.Optional;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

public class Stats implements SlashCommand {
    public void execute(SlashCommandInteraction interaction) {
        final Server server = interaction.getServer().get();
        User user = interaction.getUser();
        if (!interaction.getArguments().isEmpty()) {
            user = interaction.getArguments().get(0).getUserValue().get();
        }

        final RedisUserEntry userEntry = new RedisUserEntry(user.getId());

        final int currentLevel = userEntry.getLevel();
        final int currentStreak = userEntry.getInt("streak");

        final int xpForLevelUp =  XPSystemCalculator.determineMinimumUserFacingXPForLevel(currentLevel + 1);
        final String highScore = String.valueOf(userEntry.getInt("highscore"));

        final Optional<Color> roleColor = server.getRoleColor(user);

        final int numberOfFilledInCircles = Math.round((float) (userEntry.getXP() - (float) XPSystemCalculator.determineMinimumTotalXPForLevel(currentLevel)) / (float) xpForLevelUp * 20f);
        final StringBuilder progressCircles = new StringBuilder();

        for (int i = 0; i < numberOfFilledInCircles; i++) {
            progressCircles.append("●");
        }
        for (int i = 0; i < 20 - numberOfFilledInCircles; i++) {
            progressCircles.append("○");
        }

        final long currentRankRoleId = RankUtil.getRankFromLevel(currentLevel).getId();

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(user.getDisplayName(server))
            .setThumbnail(user.getEffectiveAvatar(interaction.getServer().get()))
            .setColor(roleColor.orElse(Values.HILDA_BLUE))
            .setDescription((userEntry.hasKey("tagline") ? "**" + userEntry.getString("tagline") + "**\n" : "") + String.format("Level %d • %s", currentLevel, server.getRoleById(currentRankRoleId).get().getName()))
            .addField("Progress", progressCircles.toString())
            .addInlineField("XP", userEntry.getXP() - XPSystemCalculator.determineMinimumTotalXPForLevel(currentLevel) + "/" + xpForLevelUp)
            .addInlineField("Streak", String.valueOf(currentStreak))
            .addInlineField("High Score", highScore)
            .addField("Kudos", String.format("<:HildaStar:539313415425097728> **Given:** %d | <:HildaStar:539313415425097728> **Received:** %d", userEntry.getInt("kudos:given"), userEntry.getInt("kudos:received")))
            .addField("Stats", "**Submits:** " + userEntry.getInt("timessubmitted"))
            .addField("Submit Status", userEntry.getBoolean("submitted") ? (user.getId() == interaction.getUser().getId()) ? ":white_check_mark: You have submitted today!" : ":white_check_mark: Submitted today!" : (user.getId() == interaction.getUser().getId()) ? "You have not submitted today." : "Nothing yet today!");

        final String streakExpiry = userEntry.getString("streakexpiry");
        if (streakExpiry != null) {
            embed.addField("Streak Expiry", "<t:" + streakExpiry + ">");
        }

        interaction.createImmediateResponder().addEmbed(embed).respond();
    }

}
