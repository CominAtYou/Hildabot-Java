package com.cominatyou;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.cominatyou.commands.*;
import com.cominatyou.commands.admin.*;
import com.cominatyou.commands.staff.InitializeUser;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class TextCommandHandler {
    // Commands that can be run in both private channels and guild text channels
    private static final Map<String, TextCommand> sharedCommands = Map.ofEntries(
        entry("levelalert", new LevelAlert()),
        entry("querydb", new QueryDatabase()),
        entry("streakwarning", new StreakWarning()),
        entry("setactivity", new SetActivity())
    );

    // Commands that can be run in only guild text channels
    private static final Map<String, TextCommand> guildCommands = Map.ofEntries(
        entry("birthday", new Birthdays()),
        entry("stats", new Stats()),
        entry("submit", new Submit()),
        entry("help", new Help()),
        entry("commit", new Commit()),
        entry("say", new Say()),
        entry("version", new GetVersion()),
        entry("levelcheck", new LevelCheck()),
        entry("ping", new Ping()),
        entry("dbcard", new DbEntryCard()),
        entry("type", new SendTyping()),
        entry("inituser", new InitializeUser()),
        entry("creationdate", new CreationDate()),
        entry("runbirthdays", new RunBirthdays()),
        entry("timeleft", new TimeLeft())
    );

    public static void getCommand(MessageCreateEvent event) {
        final String[] messageContentArray = event.getMessageContent().substring(Config.PREFIX.length()).split(" +");
        final ArrayList<String> messageArgs = new ArrayList<>(Arrays.asList(messageContentArray));

        final String command = messageArgs.remove(0).toLowerCase();

        if (sharedCommands.containsKey(command)) {
            sharedCommands.get(command).execute(event, messageArgs);
        }
        else if (event.getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL && guildCommands.containsKey(command)) {
            guildCommands.get(command).execute(event, messageArgs);
        }
    }
}
