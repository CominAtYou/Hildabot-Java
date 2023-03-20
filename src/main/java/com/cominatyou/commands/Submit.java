package com.cominatyou.commands;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import com.cominatyou.TextCommand;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Submit implements TextCommand {
    private static final List<Long> allowedChannels = Arrays.asList(492580926111481859L, 492580873628286976L, 492578733442465804L, 492579714674720778L, 492885164993675264L, 565327145786802176L);

    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!allowedChannels.contains(message.getChannel().getId())) return;
        if (!String.join(" ", messageArgs).matches("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*") && message.getMessage().getAttachments().size() == 0) {
            message.getMessage().reply("You need to provide something to submit!");
            return;
        }

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final ZonedDateTime now = ZonedDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final ZonedDateTime midnightToday = now.toLocalDate().atStartOfDay(now.getZone());

        if (user.getBoolean("submitted")) {
            final ZonedDateTime midnightTomorrow = midnightToday.plusDays(1);
            message.getMessage().reply("You've already submitted today! You can submit again <t:" + midnightTomorrow.toEpochSecond() + ":R>.");
            return;
        }

        final int streak = user.getInt("streak");

        final ZonedDateTime midnightInAWeek = midnightToday.plusDays(7);
        final long streakExpiry = midnightInAWeek.toEpochSecond();
        final List<String> submitBoosts = user.getList("items:submitboosts");

        final int currentLevel = user.getLevel();

        // Remove old streak entry for warnings.
        final long oldExpirySec = user.getLong("streakexpiry");
        if (oldExpirySec != 0) {
            final ZonedDateTime oldExpiry = Instant.ofEpochSecond(oldExpirySec).atZone(Values.TIMEZONE_AMERICA_CHICAGO);
            final int month = oldExpiry.getMonthValue();
            final int day = oldExpiry.getDayOfMonth();

            final String key = String.format("streakexpiries:%d:%d", month, day);

            RedisInstance.getInstance().lrem(key, 0, user.getIdAsString());
        }

        // Increment the user's streak.
        user.incrementKey("streak");
        // Streaks expire after one week of inactivity.
        user.expireKeyAt("streak", streakExpiry);
        // XP for submissions is calculated by taking the user's current streak, and adding 20 to it.
        int amount = 20 + 2 * streak;
        // If the user has a submit boost, apply it.
        double multiplier = 1;
        if (!submitBoosts.isEmpty()) {
            multiplier = Double.parseDouble(user.listShift("items:submitboosts"));
            amount *= multiplier;
        }
        // Give the user their XP.
        user.incrementKey("xp", amount);
        // Record the message ID of the submission.
        user.set("latestsubmissionid", message.getMessage().getIdAsString());
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
        // Give the user 30 tokens for submitting.
        final long tokens = user.incrementKey("tokens", 30);

        // Add streak expiry to database for warnings
        final int expiryMonth = midnightInAWeek.getMonthValue();
        final int expiryDay = midnightInAWeek.getDayOfMonth();
        final String newExpiryKey = String.format("streakexpiries:%d:%d", expiryMonth, expiryDay);

        RedisInstance.getInstance().rpush(newExpiryKey, user.getIdAsString());
        if (RedisInstance.getInstance().ttl(newExpiryKey) < 0) {
            RedisInstance.getInstance().expireat(newExpiryKey, streakExpiry);
        }

        // If the streak is higher than the user's current high score, set the high score to that.
        if (user.getInt("highscore") < streak + 1) {
            // streak contains the pre-increment value, so + 1
            user.set("highscore", String.valueOf(streak + 1));
        }

        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Submission successful!")
            .setAuthor(message.getMessageAuthor())
            .setDescription("Your submission has been accepted!")
            .setColor(Values.SUCCESS_GREEN)
            .addInlineField("Streak", String.valueOf(streak + 1))
            .addInlineField("XP Gained", amount + (multiplier == 1 ? "" : String.format(" (%.2fx)", multiplier)))
            .addInlineField("Tokens Received", String.format("30 (Total: %d)", tokens))
            .addField("Streak Expiry", String.format("<t:%d>", streakExpiry))
            .setFooter("Tokens can be used with the /store command.");
        message.getChannel().sendMessage(embed);

        XPSystem.checkForLevelUp(currentLevel, message.getMessageAuthor().asUser().get(), message.getServer().get());
    }

    public String getName() {
        return "Submit";
    }
}
