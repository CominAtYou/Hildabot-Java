package com.cominatyou.commands;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Submit {
    private static final List<Long> allowedChannels = Arrays.asList(492580926111481859L, 492580873628286976L, 492578733442465804L, 492579714674720778L, 492885164993675264L);
    public static void acceptSubmission(MessageCreateEvent message, List<String> messageArgs) {
        if (!allowedChannels.contains(message.getChannel().getId())) return;
        if (!String.join(" ", messageArgs).matches("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*") && message.getMessage().getAttachments().size() == 0) {
            message.getMessage().reply("You need to provide something to submit!");
            return;
        }

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());

        if (user.getBoolean("submitted")) {
            message.getMessage().reply("You've already submitted today!");
            return;
        }

        final int streak = user.getInt("streak");

        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Chicago"));
        final ZonedDateTime midnightToday = now.toLocalDate().atStartOfDay(now.getZone());
        final long streakExpiry = midnightToday.plus(7, ChronoUnit.DAYS).toEpochSecond();

        final int currentLevel = user.getLevel();

        // Increment the user's streak.
        user.incrementKey("streak");
        // Streaks expire after one week of inactivity.
        user.expireKeyAt("streak", streakExpiry);
        // XP for submissions is calculated by taking the user's current streak, and adding 20 to it.
        user.incrementKey("xp", 20 + 2 * streak);
        // One submission is permitted per day.
        user.set("submitted", "true");
        // Increment the total number of submissions from this user.
        user.incrementKey("timessubmitted");
        // Allow submissions after midnight CT.
        user.expireKeyAt("submitted", midnightToday.plus(1, ChronoUnit.DAYS).toEpochSecond());
        // Record streak expiry timestamp.
        user.set("streakexpiry", String.valueOf(streakExpiry));
        // Remove timestamp when streak expires.
        user.expireKeyAt("streakexpiry", streakExpiry);

        // If the streak is higher than the user's current high score, set the high score to that.
        if (user.getInt("highscore") < streak + 1) {
            // streak contains the pre-increment value, so + 1
            user.set("highscore", String.valueOf(streak + 1));
        }

        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Submission successful!")
            .setAuthor(message.getMessageAuthor())
            .setDescription("Your submission has been accepted!")
            .setColor(new java.awt.Color(Values.SUCCESS_GREEN))
            .addInlineField("Streak", String.valueOf(streak + 1))
            .addInlineField("XP Gained", String.valueOf(20 + 2 * streak))
            .addField("Streak Expiry", String.format("<t:%d>", streakExpiry));
        message.getChannel().sendMessage(embed);

        XPSystem.checkForLevelUp(currentLevel, message);
    }
}
