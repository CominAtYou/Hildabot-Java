package com.cominatyou.util;

import com.cominatyou.util.versioning.Version;

import org.javacord.api.BotInviteBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Permissions;

public class StartupInfo {
    public static void output(DiscordApi client) {
        System.out.printf("Hildabot %s (%s)\n", Version.VERSION, Version.BUILD_NUMBER);

        System.out.printf("Logged in as %s\n", client.getYourself().getDiscriminatedName());
        System.out.println("Invite bot with " + new BotInviteBuilder(client.getClientId()).setPermissions(Permissions.fromBitmask(412652792896L)).build() + "\n");

        if (!System.getProperty("os.name").equalsIgnoreCase("Linux")) {
            System.out.println("\u001B[33mHost is not running Linux, assuming this is a testing environment.\u001B[0m");
        }
    }
}
