package com.cominatyou.commands.staff;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.cominatyou.Command;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

public class RevertSubmission implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        final Server hildacord = message.getServer().get();
        final Role staffRole = hildacord.getRoleById(492577085743824906L).get();

        if (!message.getMessageAuthor().asUser().get().getRoles(hildacord).contains(staffRole) && message.getMessageAuthor().getId() != Values.COMIN_USER_ID) return;
        final Optional<Message> potentialReferencedMessage = message.getMessage().getReferencedMessage();

        if (potentialReferencedMessage.isEmpty()) {
            message.getChannel().sendMessage("You need to reply to the original submission for me to revert!");
            return;
        }
        final Message referencedMessage = potentialReferencedMessage.get();

        if (!referencedMessage.getContent().startsWith("h!submit")) {
            message.getChannel().sendMessage("That message doesn't seem to be a submission.");
            return;
        }

        final RedisUserEntry authorEntry = new RedisUserEntry(referencedMessage.getAuthor());
        final Long authorId = authorEntry.getId();

        // Relevant keys: streak (int), submitted (boolean), timessubmitted (int), streak (int), streakexpiry (long), laststreakexpiry (long)

        if (referencedMessage.getId() != authorEntry.getLong("latestsubmissionid")) {
            message.getChannel().sendMessage("I can't revert that submission, as it isn't this member's latest submission.");
            return;
        }

        if (authorEntry.getInt("streak") == 1) {
            final Long streakExpiry = authorEntry.getLong("streakexpiry");

            final ZonedDateTime localizedStreakExpiry = ZonedDateTime.ofInstant(Instant.ofEpochSecond(streakExpiry), Values.TIMEZONE_AMERICA_CHICAGO);
            final int month = localizedStreakExpiry.getMonthValue();
            final int day = localizedStreakExpiry.getDayOfMonth();
            final String expiriesListKey = String.format("streakexpiries:%d:%d", month, day);

            RedisInstance.getInstance().del("users:" + authorId + ":streak", "users:" + authorId + ":submitted", "users:" + authorId + ":streakexpiry", "users:" + authorId + ":latestsubmissionid");
            authorEntry.decrementKey("xp", 20);
            authorEntry.decrementKey("timessubmitted");
            RedisInstance.getInstance().lrem(expiriesListKey, 0, authorId.toString());

            message.getChannel().sendMessage("That submission has been reverted.");
            return;
        }

        if (authorEntry.getInt("highscore") == authorEntry.getInt("streak")) {
            authorEntry.decrementKey("highscore");
        }
        final long currentStreakExpiry = authorEntry.getLong("streakexpiry");
        final Long previousStreakExpiry = authorEntry.getLong("previousstreakexpiry");

        authorEntry.decrementKey("xp", 20 + 2 * authorEntry.getInt("streak"));
        authorEntry.decrementKey("streak");
        authorEntry.expireKeyAt("streak", previousStreakExpiry);
        authorEntry.decrementKey("timessubmitted");
        authorEntry.set("streakexpiry", previousStreakExpiry.toString());
        authorEntry.expireKeyAt("streakexpiry", previousStreakExpiry);
        RedisInstance.getInstance().del("users:" + authorId + ":submitted", "users:" + authorId + ":previousstreakexpiry", "users:" + authorId + ":latestsubmissionid");

        final ZonedDateTime localizedStreakExpiry = ZonedDateTime.ofInstant(Instant.ofEpochSecond(currentStreakExpiry), Values.TIMEZONE_AMERICA_CHICAGO);
        final ZonedDateTime localizedPreviousStreakExpiry = ZonedDateTime.ofInstant(Instant.ofEpochSecond(previousStreakExpiry), Values.TIMEZONE_AMERICA_CHICAGO);

        final String expiriesListKey = String.format("streakexpiries:%d:%d", localizedStreakExpiry.getMonthValue(), localizedStreakExpiry.getDayOfMonth());
        final String newExpiriesEntryKey = String.format("streakexpiries:%d:%d", localizedPreviousStreakExpiry.getMonthValue(), localizedPreviousStreakExpiry.getDayOfMonth());

        RedisInstance.getInstance().lrem(expiriesListKey, 0, authorId.toString());
        RedisInstance.getInstance().rpush(newExpiriesEntryKey, authorId.toString());

        message.getChannel().sendMessage("That submission has been reverted.");
    }

    public String getName() {
        return "RevertSubmission";
    }
}
