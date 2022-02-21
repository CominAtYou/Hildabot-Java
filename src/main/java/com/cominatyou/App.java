package com.cominatyou;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.eventhandlers.MessageCreate;
import com.cominatyou.eventhandlers.kudos.Kudos;
import com.cominatyou.eventhandlers.memberevents.*;
import com.cominatyou.routinetasks.RoutineTasks;
import com.cominatyou.util.activities.ActivitySwapper;

import org.javacord.api.BotInviteBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.permission.Permissions;

public class App {
    private static final DiscordApi client = new DiscordApiBuilder().setToken(Config.TOKEN).setIntents(Intent.GUILD_MEMBERS, Intent.GUILD_MESSAGES, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGE_REACTIONS).login().join();
    public static DiscordApi getClient() {
        return client;
    }

    public static void main(String[] args) {
        System.out.printf("Logged in as %s\n", client.getYourself().getDiscriminatedName());
        System.out.println("Invite bot with " + new BotInviteBuilder(client.getClientId()).setPermissions(Permissions.fromBitmask(412652792896L)).build());

        // Open connection to Redis database
        RedisInstance.connect();

        // Schedule routine tasks (i.e. birthday announcements)
        RoutineTasks.schedule();

        // Set the bot's activity.
        ActivitySwapper.start(client);

        client.addMessageCreateListener(MessageCreate::route);

        client.addServerMemberJoinListener(MemberJoin::greet);

        client.addServerMemberLeaveListener(MemberLeave::removeDBEntries);
        client.addServerMemberBanListener(MemberBan::removeDBEntries);

        client.addReactionAddListener(Kudos::tally);
        client.addReactionRemoveListener(Kudos::remove);
    }
}
