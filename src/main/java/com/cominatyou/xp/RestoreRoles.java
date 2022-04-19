package com.cominatyou.xp;

import java.util.Arrays;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.logging.Log;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

public class RestoreRoles {
    public static void restore(ServerMemberJoinEvent event) {
        final RedisUserEntry user = new RedisUserEntry(event.getUser());
        if (user.getXP() < 290) return;

        final int rankLevel = RankUtil.getRankFromLevel(user.getLevel()).getLevel();

        final Integer[] rankLevels = RankUtil.getRanklevels();
        final int index = Arrays.asList(rankLevels).indexOf(rankLevel);

        for (int i = index; i > 0; i--) {
            final long levelId = RankUtil.getRankFromLevel(rankLevels[i]).getId();
            final Role role = event.getServer().getRoleById(levelId).get();
            role.addUser(event.getUser());
        }
        
        Log.eventf("RESTORE", "Restored roles for %s (%d), starting from level %d\n", event.getUser().getDiscriminatedName(), event.getUser().getId(), rankLevel);
    }
}
