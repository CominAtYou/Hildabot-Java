package com.cominatyou.xp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.ThousandsFormat;
import com.cominatyou.util.Values;
import com.cominatyou.util.logging.Log;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class XPSystem {
    private final static List<Long> ignoredChannels = Arrays.asList(Values.BOT_CHANNEL);

    public static void giveXPForMessage(MessageCreateEvent message) {
        if (ignoredChannels.contains(message.getChannel().getId())) return;

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());

        // XP will only be granted for 7 messages sent within 30 seconds.
        if (user.getInt("recentmessagecount") == 7) return;
        if (RedisInstance.getInstance().lrange("config:xp:ignoredusers", 0, -1).contains(message.getMessageAuthor().getIdAsString())) return;

        final int currentXP = user.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        // Award XP.
        final int amount = ThreadLocalRandom.current().nextInt(2, 5 + 1); // random number between 2 and 5, inclusive
        user.incrementKey("xp", amount);

        // Check if rate limit key exists. If not, create it, set it to 1, and have it expire in 30 seconds.
        if (!user.hasKey("recentmessagecount")) {
            user.set("recentmessagecount", "1");
            user.expireKeyIn("recentmessagecount", 30);
        }
        else {
            // Incrememnt the rate limit key by 1 (this does not reset the expiration timer)
            user.incrementKey("recentmessagecount");
        }

        checkForLevelUp(currentLevel, message.getMessageAuthor().asUser().get(), message.getServer().get());
    }

    public static void checkForLevelUp(int beforeActionLevel, User user, Server server) {
        final RedisUserEntry userEntry = new RedisUserEntry(user);

        final int currentXP = userEntry.getXP();
        final int currentLevel = XPSystemCalculator.determineLevelFromXP(currentXP);

        if (currentLevel > beforeActionLevel) {
            final String rankUpMessage = String.format("Congrats on leveling up! You've reached level **%d** and are now the **%s** rank! :tada:", currentLevel, RankUtil.getRankFromLevel(currentLevel).getName());
            final String levelUpMessage = String.format("Congrats on leveling up! You are now level **%d**! :tada:", currentLevel);
            final String embedTitle = RankUtil.isLevelRankLevel(currentLevel) ? rankUpMessage : levelUpMessage;

            Log.eventf("LEVELUP", "%s (%d) leveled up to %d: %s XP\n", user.getDiscriminatedName(), user.getId(), currentLevel, ThousandsFormat.format(currentXP));

            if (!userEntry.getBoolean("levelalertsdisabled")) {
                final EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Values.HILDA_BLUE)
                        .setTitle(embedTitle)
                        .setDescription("To disable this message going forward, run `h!levelalert` in this DM or <#495034452422950915>.");
                try {
                    user.openPrivateChannel().join().sendMessage(embed);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (RankUtil.isLevelRankLevel(currentLevel)) {
                final long roleId = RankUtil.getRankFromLevel(currentLevel).getId();
                final Role role = server.getRoleById(roleId).get();

                userEntry.incrementKey("tokens", 40);

                final EmbedBuilder errorEmbed = new EmbedBuilder().setColor(Values.HILDA_BLUE)
                    .setTitle("Failed to assign a role!")
                    .setDescription("Role assignment for a level up did not complete successfully. Check `hildabot-error.log` for more information.")
                    .addInlineField("User", String.format("%s (%d)", user.getDiscriminatedName(), user.getId()))
                    .addInlineField("Role", String.format("%s (%d)", role.getName(), currentLevel))
                    .setColor(Values.HILDA_BLUE);
                try {
                    role.addUser(user).get();
                    Log.eventf("LEVELUP", "Assigned role %s to %s (%d)\n", role.getName(), user.getDiscriminatedName(), user.getId());
                }
                catch (Exception e) {
                    Log.errorf("LEVELUP", "Failed to assign %s to %s (%d)!\n", role.getName(), user.getDiscriminatedName(), user.getId());
                    user.getApi().getOwner().get().join().openPrivateChannel().join().sendMessage(errorEmbed);

                    if (e.getCause() == null) e.printStackTrace();
                    else e.getCause().printStackTrace();
                }
            }
            else {
                userEntry.incrementKey("tokens", 20);
            }
        }
    }
}
