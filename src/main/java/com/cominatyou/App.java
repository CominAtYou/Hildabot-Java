package com.cominatyou;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.routinetasks.RoutineTasks;
import com.cominatyou.util.MessageValidity;
import com.cominatyou.util.activities.ActivitySwapper;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.BotInviteBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.permission.Permissions;

public class App {
    private static final DiscordApi client = new DiscordApiBuilder().setToken(Config.TOKEN).setIntents(Intent.GUILD_MEMBERS, Intent.GUILD_MESSAGES, Intent.DIRECT_MESSAGES).login().join();
    public static DiscordApi getClient() {
        return client;
    }

    public static void main(String[] args) {
        System.out.printf("Logged in as %s\n", client.getYourself().getDiscriminatedName());
        System.out.println("Invite bot with " + new BotInviteBuilder(client.getClientId()).setPermissions(Permissions.fromBitmask(8)).build()); // TODO: Change this after completion

        // Open connection to Redis database
        RedisInstance.connect();

        // Schedule routine tasks (i.e. birthday announcements)
        RoutineTasks.schedule();

        // Set the bot's activity.
        ActivitySwapper.start(client);

        client.addMessageCreateListener(event -> {
            if (!MessageValidity.test(event)) return;
            if (!event.getMessage().getContent().startsWith(Config.PREFIX) && event.getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL) {
                XPSystem.giveXPForMessage(event);
            }
            else {
                TextCommandHandler.getCommand(event);
            }
        });

        client.addServerMemberJoinListener(Welcome::greet);
    }
}
