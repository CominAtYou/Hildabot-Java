package com.cominatyou.console.consolecommands;

import java.util.List;

import org.javacord.api.entity.channel.ServerTextChannel;

import com.cominatyou.console.ConsoleChannel;

public class Send extends ChannelBasedConsoleCommand implements ConsoleCommand {
    public void execute(List<String> args) {
        super.execute(args);
    }

    protected void command(List<String> args) {
        final ServerTextChannel channel = ConsoleChannel.getCurrentChannel();

        if (args.size() == 0) {
            System.err.println("You need to provide a message to send!");
            return;
        }

        channel.sendMessage(String.join(" ", args));
    }

}
