package com.cominatyou.console;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;

import com.cominatyou.console.consolecommands.*;

/**
 * A command-line interface that can be used to execute special commands
 * within the bot, without having to invoke the bot through Discord.
 */
public class Console {
    private static final Map<String, ConsoleCommand> commands = Map.ofEntries(
        entry("delete", new Delete()),
        entry("reply", new Reply()),
        entry("send", new Send()),
        entry("cc", new ChangeChannel()),
        entry("edit", new Edit()),
        entry("clear", new Clear())
    );

    /**
     * Start the shell.
     * @param client The {@code DiscordApi} instance that the shell should use.
     */
    public static void init(DiscordApi client) {
        @SuppressWarnings("all")
        Scanner scanner = new Scanner(System.in);

        new Thread("Hildabot Console") {
            public void run() {
                while (true) {
                    final ServerTextChannel channel = ConsoleChannel.getCurrentChannel();
                    System.out.print("Hildabot" + (ConsoleChannel.isChannelSet() ? " " + Colors.BLUE + channel.getServer().getName() + Colors.RESET + "/" + Colors.GREEN + "#" + channel.getName() + Colors.RESET : "") + "> ");

                    final String input = scanner.nextLine();
                    final ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" +")));
                    final String command = args.remove(0);

                    if (command.length() == 0) continue;

                    if (!commands.containsKey(command)) {
                        System.err.println(command + ": command not found");
                        continue;
                    }

                    commands.get(command).execute(args);
                }
            }
        }.start();
    }
}
