package com.cominatyou.commands;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Submit {
    private static final List<Long> allowedChannels = Arrays.asList(492580926111481859L, 492580873628286976L, 492578733442465804L, 492579714674720778L);
    public static void acceptSubmission(MessageCreateEvent message, List<String> messageArgs) {
        // TODO: Clear this for testing
        if (!allowedChannels.contains(message.getChannel().getId())) return;
        if (!String.join(" ", messageArgs).matches("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*") && message.getMessage().getAttachments().size() == 0) {
            message.getMessage().reply("You need to provide something to submit!");
            return;
        }

        final long messageAuthorId = message.getMessageAuthor().getId();

        if (RedisInstance.getBoolean("users:" + messageAuthorId + ":submitted")) {
            message.getMessage().reply("You've already submitted today!");
            return;
        }

        final int streak = RedisInstance.getInt("users:" + message.getMessageAuthor().getIdAsString() + ":streak");

        final ZonedDateTime now = ZonedDateTime.now(Clock.systemDefaultZone());
        final ZonedDateTime midnightToday = now.toLocalDate().atStartOfDay(now.getZone());
        final long streakExpiry = midnightToday.plus(7, ChronoUnit.DAYS).toEpochSecond();

        final int currentLevel = new RedisUserEntry(messageAuthorId).getLevel();

        // Increment the user's streak.
        RedisInstance.getInstance().incr("users:" + messageAuthorId + ":streak");
        // Streaks expire after one week of inactivity.
        RedisInstance.getInstance().expireat("users:" + messageAuthorId + ":streak", streakExpiry);
        // XP for submissions is calculated by taking the user's current streak, and adding 20 to it.
        RedisInstance.getInstance().incrby("users:" + messageAuthorId + ":xp", 20 + 2 * streak);
        // One submission is permitted per day.
        RedisInstance.getInstance().set("users:" + messageAuthorId + ":submitted", "true");
        // Increment the total number of submissions from this user.
        RedisInstance.getInstance().incr("users:" + messageAuthorId + ":timessubmitted");
        // Allow submissions after midnight CT.
        RedisInstance.getInstance().expireat("users:" + messageAuthorId + ":submitted", midnightToday.plus(1, ChronoUnit.DAYS).toEpochSecond());
        // Record streak expiry timestamp.
        RedisInstance.getInstance().set("users:" + messageAuthorId + ":streakexpiry", String.valueOf(streakExpiry));
        // Remove timestamp when streak expires.
        RedisInstance.getInstance().expireat("users:" + messageAuthorId + ":streakexpiry", streakExpiry);

        // If the streak is higher than the user's current high score, set the high score to that.
        if (RedisInstance.getInt("users:" + messageAuthorId + ":highscore") < streak + 1) {
            // streak contains the pre-increment value, so + 1
            RedisInstance.getInstance().set("users:" + messageAuthorId + ":highscore", String.valueOf(streak + 1));
        }

        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Submission successful!")
            .setAuthor(message.getMessageAuthor())
            .setDescription("Your submission has been accepted! Your XP and streak have been updated accordingly.")
            .setColor(new java.awt.Color(Values.SUCCESS_GREEN))
            .addInlineField("Streak", String.valueOf(streak + 1))
            .addInlineField("XP Gained", String.valueOf(20 + 2 * streak))
            .addField("Streak Expires", String.format("<t:%d>", streakExpiry));
        message.getChannel().sendMessage(embed);

        XPSystem.checkForLevelUp(currentLevel, message);
    }
}
