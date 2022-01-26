package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.RedisUserEntry;
import com.cominatyou.util.RedisInstance;
import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class Restore {
    public void restoreRank(MessageCreateEvent message) {
        final int[] rankLevels = RankUtil.getRanklevels();
        final List<Role> userRoles = message.getMessageAuthor().asUser().get().getRoles(message.getServer().get());
        for (int i = rankLevels.length -1; i > -1; i--) {
            final Rank rank = RankUtil.getRankFromLevel(rankLevels[i]);
            final Role role = message.getServer().get().getRoleById(rank.getId()).get();
            if (userRoles.contains(role)) {
                message.getMessage().reply(String.format("You're good to go! Your rank has been set to %s and your level has been set to %d.", rank.getName(), rank.getLevel()));
                final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
                final Integer xpToGive = XPSystemCalculator.determineMinimumXPForLevel(rank.getLevel());
                RedisInstance.getInstance().set(user.getRedisKey() + ":xp", xpToGive.toString());
                return;
            }
        }
        message.getMessage().reply("Something went wrong! Make sure you have at least the Time Worm role, and then try again! Please contact Comin if it still doesn't work after that.");
    }
}
