package com.cominatyou.commands;

import java.util.HashMap;
import java.util.List;

import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class Restore {
    public void restoreRank(MessageCreateEvent message) {
        final int[] rankLevels = RankUtil.getRanklevels();
        final List<Role> userRoles = message.getMessageAuthor().asUser().get().getRoles(message.getServer().get());
        final HashMap<Integer, Rank> rankMap = RankUtil.getRankMap();
        for (int i = rankLevels.length; i > -1; i--) {
            final Rank rank = rankMap.get(rankLevels[i]);
            final Role role = message.getServer().get().getRoleById(rank.getId()).get();
            if (userRoles.contains(role)) {
                message.getMessage().reply(String.format("You're good to go! Your rank has been set to %s and your level has been set to %d.", rank.getName(), rank.getLevel()));
                
            }
        }
    }
}
