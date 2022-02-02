package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class Restore {
    public static void restoreRank(MessageCreateEvent message) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        if (user.isEnrolled()) {
            message.getMessage().reply("Looks like you've already done this before!");
            return;
        }

        final int[] rankLevels = RankUtil.getRanklevels();
        final MessageAuthor author = message.getMessageAuthor();
        final List<Role> userRoles = author.asUser().get().getRoles(message.getServer().get());

        for (int i = rankLevels.length - 1; i > -1; i--) {
            final Rank rank = RankUtil.getRankFromLevel(rankLevels[i]);
            final Role role = message.getServer().get().getRoleById(rank.getId()).get();
            
            if (userRoles.contains(role)) {
                message.getMessage().reply(String.format("You're good to go! Your rank has been set to %s and your level has been set to %d.", rank.getName(), rank.getLevel()));
                final Integer xpToGive = XPSystemCalculator.determineMinimumTotalXPForLevel(rank.getLevel());
                RedisInstance.getInstance().set(user.getRedisKey() + ":xp", xpToGive.toString());
                RedisInstance.getInstance().set(user.getRedisKey() + ":enrolled", "true");
                return;
            }
        }
        message.getMessage().reply("Something went wrong! Make sure you have at least the Time Worm role, and then try again! Please contact Comin if it still doesn't work after that.");
    }
}
