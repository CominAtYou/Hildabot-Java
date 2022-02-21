package com.cominatyou.xp;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.logging.Log;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

public class RestoreRoles {
    public static void restore(ServerMemberJoinEvent event) {
        final RedisUserEntry user = new RedisUserEntry(event.getUser());
        if (user.getXP() < 290) return;

        final int rankLevel = RankUtil.getRankFromLevel(user.getLevel()).getLevel();

        final int[] rankLevels = RankUtil.getRanklevels();
        // This is a really really really stupid idea. It works, though.
        final int index = IntStream.of(rankLevels).boxed().collect(Collectors.toList()).indexOf(rankLevel);

        for (int i = index; i > 0; i--) {
            final long levelId = RankUtil.getRankFromLevel(rankLevels[i]).getId();

            final Role role = event.getServer().getRoleById(levelId).get();
            role.addUser(event.getUser());
        }

        Log.eventf("RESTORE", "Restored roles for %s (%d), starting from level %d", event.getUser().getDiscriminatedName(), event.getUser().getId(), rankLevel);

    }
}
