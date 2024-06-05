package com.cominatyou;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import com.cominatyou.console.Console;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.eventhandlers.*;
import com.cominatyou.eventhandlers.memberevents.*;
import com.cominatyou.routinetasks.RoutineTasks;
import com.cominatyou.util.ShutdownHook;
import com.cominatyou.util.StartupInfo;
import com.cominatyou.util.activities.ActivitySwapper;

public class App {
    private static final DiscordApi client = new DiscordApiBuilder()
        .setToken(Config.TOKEN)
        .setIntents(Intent.GUILD_MEMBERS, Intent.GUILD_MESSAGES, Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGE_REACTIONS)
        .login()
        .join();

    public static DiscordApi getClient() {
        return client;
    }

    public static void main(String[] args) {
        // Write useful info to the output
        StartupInfo.output(client);

        // Open connection to Redis database
        RedisInstance.connect();

        // Schedule routine tasks (i.e. birthday announcements)
        RoutineTasks.schedule();

        // Set the bot's activity.
        ActivitySwapper.start(client);

        // Initialize the shutdown hook.
        ShutdownHook.init();

        client.addMessageCreateListener(MessageCreate::route);
        client.addSlashCommandCreateListener(SlashCommandHandler::route);

        client.addSelectMenuChooseListener(SelectMenuChoose::route);
        client.addButtonClickListener(ButtonClick::route);

        client.addUserChangePendingListener(MemberPassGate::greet);

        client.addReactionAddListener(Kudos::tally);
        client.addReactionRemoveListener(Kudos::remove);

        Console.init(client);
    }
}
