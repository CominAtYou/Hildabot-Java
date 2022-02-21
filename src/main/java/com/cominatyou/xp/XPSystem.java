package com.cominatyou.xp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.util.logging.Log;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class XPSystem {
    private final static List<Long> ignoredChannels = Arrays.asList(Values.BOT_CHANNEL);

    public static void giveXPForMessage(MessageCreateEvent message) {
        if (ignoredChannels.contains(message.getChannel().getId())) return;

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());

        // XP will only be granted for 7 messages sent within 30 seconds. This might
        // need to be increased.
        if (user.getInt("recentmessagecount") == 7) return;

        final int currentXP = user.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        // Award XP.
        final int amount = ThreadLocalRandom.current().nextInt(2, 4 + 1); // random number between 2 and 4, inclusive
        user.incrementKey("xp", amount);

        // Check if rate limit key exists. If not, create it, set it to 1, and have it
        // expire in 30 seconds.
        if (user.getString("recentmessagecount") == null) {
            user.set("recentmessagecount", "1");
            user.expireKeyIn("recentmessagecount", 30);
        }
        else {
            // Incrememnt the rate limit key by 1 (this does not reset the expiration timer)
            user.incrementKey("recentmessagecount");
        }

        checkForLevelUp(currentLevel, message);
    }

    public static void checkForLevelUp(int beforeActionLevel, MessageCreateEvent message) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());

        final int currentXP = user.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        if (currentLevel > beforeActionLevel) {
            final String embedTitle = RankUtil.isLevelRankLevel(currentLevel) ? String.format("Congrats on leveling up! You've reached level **%d** and are now the **%s** rank!", currentLevel, RankUtil.getRankFromLevel(currentLevel).getName()) : String.format("Congrats on leveling up! You are now level **%d**! :tada:", currentLevel);

            // Time for logging purposes
            final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            final LocalDateTime currentTime = LocalDateTime.now();
            final String timeString = timeFormat.format(currentTime);

            Log.eventf("LEVELUP", "%s (%d) leveled up to %d: %d XP\n", timeString, message.getMessageAuthor().getDiscriminatedName(), user.getId(), currentLevel, currentXP);

            if (!RedisInstance.getBoolean("users:" + user.getId() + ":levelalertsdisabled")) {
                final EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new java.awt.Color(Values.HILDA_BLUE))
                        .setTitle(embedTitle)
                        .setDescription("To disable this message going forward, run `h!levelalert` in this DM or <#495034452422950915>.");
                message.getMessageAuthor().asUser().get().openPrivateChannel().join().sendMessage(embed);
            }

            if (RankUtil.isLevelRankLevel(currentLevel)) {
                final long roleId = RankUtil.getRankFromLevel(currentLevel).getId();
                final Role role = message.getServer().get().getRoleById(roleId).get();
                message.getMessageAuthor().asUser().get().addRole(role, message.getMessageAuthor().getName() + " leveled up!");
                Log.eventf("LEVELUP", "Assigned role %s to %s (%d)\n", role.getName(), message.getMessageAuthor().getDiscriminatedName(), message.getMessageAuthor().getId());
            }
        }
    }
}
