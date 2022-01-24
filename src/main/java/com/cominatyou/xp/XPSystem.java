package com.cominatyou.xp;

import java.util.ArrayList;

import com.cominatyou.RedisUserEntry;
import com.cominatyou.util.RedisInstance;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import io.netty.util.internal.ThreadLocalRandom;

public class XPSystem {
    private final static ArrayList<Long> ignoredChannels = new ArrayList<>();

    public static void giveXPForMessage(MessageCreateEvent message) {
        if (ignoredChannels.contains(message.getChannel().getId())) return;

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        final String redisKey = user.getRedisKey();

        // XP will only be granted for 7 messages sent within 30 seconds. This might
        // need to be increased.
        if (RedisInstance.getInt(redisKey + ":recentmessagecount") == 7) return;

        final int currentXP = user.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        // Award XP.
        int amount = ThreadLocalRandom.current().nextInt(2, 6 + 1); // random number between 2 and 6, inclusive
        user.addXP(amount);
        System.out.printf("XP awarded for %s (%d): %d XP\n", message.getMessageAuthor().getDiscriminatedName(), message.getMessageAuthor().getId(), amount);

        // Check if rate limit key exists. If not, create it, set it to 1, and have it
        // expire in 30 seconds.
        if (!RedisInstance.keyExists(redisKey + ":recentmessagecount")) {
            RedisInstance.getInstance().set(redisKey + ":recentmessagecount", "1");
            RedisInstance.getInstance().expire(redisKey + ":recentmessagecount", 30);
        }
        else {
            // Incrememnt the rate limit key by 1 (this does not reset the expiration timer)
            RedisInstance.getInstance().incr(redisKey + ":recentmessagecount");
        }

        checkForLevelUp(currentLevel, message.getMessageAuthor());
    }

    public static void checkForLevelUp(int beforeActionLevel, MessageAuthor author) {
        final RedisUserEntry user = new RedisUserEntry(author.getId());

        final int currentXP = user.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        if (currentLevel > beforeActionLevel) {
            // TODO: If the level-up grants a new role, include that in the message.
            final EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new java.awt.Color(0x007acc))
                    .setTitle(String.format("Congrats on leveling up! You are now level **%d**! :tada:", currentLevel))
                    .setDescription("To disable this message going forward, run `h!levelalert` in this DM or the bot channel in Hildacord.");
            author.asUser().get().openPrivateChannel().join().sendMessage(embed);
        }
    }
}
