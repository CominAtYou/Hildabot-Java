package com.cominatyou.util;

import org.javacord.api.entity.user.User;

import com.cominatyou.App;

public class CommandPermissions {
    /**
     * Check if a user is a staff member or is the bot owner.
     * @param user The user to check.
     * @return {@code true} if the user is a staff member or is the bot owner, {@code false} otherwise.
     */
    public static boolean canRunSensitiveCommands(User user) {
        return user.getRoles(App.getClient().getServerById(Values.BASE_GUILD_ID).get()).stream().anyMatch(role -> role.getId() == 492577085743824906L) || user.isBotOwner();
    }

    /**
     * Check if a user is a staff member.
     * @param user The user to check.
     * @return {@code true} if the user is a staff member, {@code false} otherwise.
     */
    public static boolean isStaffMember(User user) {
        return user.getRoles(App.getClient().getServerById(Values.BASE_GUILD_ID).get()).stream().anyMatch(role -> role.getId() == 492577085743824906L);
    }

    /**
     * Check if a user is an administrator.
     * @param user The user to check.
     * @return {@code true} if the user is an administrator, {@code false} otherwise.
     */
    public static boolean isAdministrator(User user) {
        return user.getRoles(App.getClient().getServerById(Values.BASE_GUILD_ID).get()).stream().anyMatch(role -> role.getId() == 492576994278637568L);
    }
}
