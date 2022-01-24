package com.cominatyou;

import com.cominatyou.util.MessageValidity;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.BotInviteBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Permissions;

public class App {
    public static void main(String[] args) {
        DiscordApi client = new DiscordApiBuilder().setToken(Config.getToken()).login().join();
        System.out.printf("Logged in as %s\n", client.getYourself().getDiscriminatedName());
        System.out.println("Invite bot with " + new BotInviteBuilder(client.getClientId()).setPermissions(Permissions.fromBitmask(8)).build()); // TODO: Change this after completion

        client.addMessageCreateListener(event -> {
            if (!MessageValidity.test(event)) return;
            // Get the command and the arguments
            if (!event.getMessage().getContent().startsWith(Config.getPrefix())) {
                XPSystem.giveXPForMessage(event);
            }
            else {
                TextCommandHandler.getCommand(event);
            }
        });
    }
}
