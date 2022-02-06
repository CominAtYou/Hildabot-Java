package com.cominatyou.commands;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class Restore {
    public static void restoreRank(MessageCreateEvent message) {
        // if (message.getChannel().getId() != Values.BOT_CHANNEL) return;
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        if (user.getBoolean("enrolled")) {
            message.getMessage().reply("Looks like you've already done this before!");
            return;
        }

        final int[] rankLevels = RankUtil.getRanklevels();
        final MessageAuthor author = message.getMessageAuthor();

        for (int i = rankLevels.length - 1; i > -1; i--) {
            final Rank rank = RankUtil.getRankFromLevel(rankLevels[i]);
            final Role role = message.getServer().get().getRoleById(rank.getId()).get();

            if (role.hasUser(author.asUser().get())) {
                message.getMessage().reply(String.format("You're good to go! Your rank has been set to %s and your level has been set to %d.", rank.getName(), rank.getLevel()));
                final Integer xpToGive = XPSystemCalculator.determineMinimumTotalXPForLevel(rank.getLevel());
                user.set("xp", xpToGive.toString());
                user.set("enrolled", "true");
                return;
            }
        }
        message.getMessage().reply("Something went wrong! Make sure you have at least the Time Worm role, and then try again! Please contact Comin if it still doesn't work after that.");
    }
}
