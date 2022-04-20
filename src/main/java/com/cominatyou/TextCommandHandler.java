package com.cominatyou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;

import com.cominatyou.commands.*;
import com.cominatyou.commands.admin.*;
import com.cominatyou.util.Command;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class TextCommandHandler {

    private static final Map<String, Command> sharedCommands = Map.ofEntries(
        entry("levelalert", new LevelAlert()),
        entry("querydb", new QueryDatabase()),
        entry("streakwarning", new StreakWarning())
    );

    private static final Map<String, Command> guildCommands = Map.ofEntries(
        entry("birthday", new Birthdays()),
        entry("stats", new Stats()),
        entry("submit", new Submit()),
        entry("restore", new Restore()),
        entry("help", new Help()),
        entry("commit", new Commit()),
        entry("say", new Say()),
        entry("version", new GetVersion()),
        entry("levelcheck", new LevelCheck()),
        entry("ping", new Ping()),
        entry("dbcard", new DbEntryCard()),
        entry("type", new SendTyping()),
        entry("inituser", new InitializeUser())
    );

    public static void getCommand(MessageCreateEvent event) {
        ArrayList<String> messageArgs = new ArrayList<>(Arrays.asList(event.getMessageContent().substring(Config.PREFIX.length()).split(" +")));
        final String command = messageArgs.remove(0).toLowerCase();

        if (sharedCommands.containsKey(command)) {
            sharedCommands.get(command).execute(event, messageArgs);
        }
        else if (event.getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL && guildCommands.containsKey(command)) {
            guildCommands.get(command).execute(event, messageArgs);
        }
    }
}
