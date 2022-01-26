package com.cominatyou.commands;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.cominatyou.util.RedisInstance;
import com.cominatyou.util.Values;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Submit {
    public static void acceptSubmission(MessageCreateEvent message, List<String> messageArgs) {
        if (messageArgs.size() == 0 && message.getMessage().getAttachments().size() == 0) {
            message.getMessage().reply("You seem to have not provided a submission!");
            return;
        }

        final long messageAuthorId = message.getMessageAuthor().getId();

        if (RedisInstance.getBoolean("users:" + messageAuthorId + ":submitted")) {
            message.getMessage().reply("You've already submitted today!");
            return;
        }

        int streak = RedisInstance.getInt("users:" + message.getMessageAuthor().getIdAsString() + ":streak");
        // Interpret streak as 0 if key does not exist.
        streak = streak == -1 ? 0 : streak;

        final Instant midnightToday = Instant.now().truncatedTo(ChronoUnit.DAYS);

        RedisInstance.getInstance().incr("users:" + messageAuthorId + ":streak");
        // Streaks expire after one week of inactivity.
        RedisInstance.getInstance().expireat("users:" + messageAuthorId + ":streak", midnightToday.plus(7, ChronoUnit.DAYS));
        // XP for submissions is calculated by taking the user's current streak, and adding 20 to it.
        RedisInstance.getInstance().incrby("users:" + message.getMessageAuthor().getIdAsString() + ":xp", 20 + 2 * streak);
        // One submission is permitted per day.
        RedisInstance.getInstance().set("users:" + messageAuthorId + ":submitted", "true");
        // Allow submissions after midnight CT.
        RedisInstance.getInstance().expireat("users:" + messageAuthorId + ":submitted", midnightToday.plus(1, ChronoUnit.DAYS));

        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Submission successful!")
            .setColor(new java.awt.Color(Values.CODE_BLUE))
            .addInlineField("Streak", String.valueOf(streak + 1))
            .addInlineField("XP Gained", String.valueOf(20 + 2 * streak));
        message.getMessage().reply(embed);
    }
}
