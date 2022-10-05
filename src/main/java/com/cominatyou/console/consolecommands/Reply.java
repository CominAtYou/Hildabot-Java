package com.cominatyou.console.consolecommands;

import java.util.List;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;

import com.cominatyou.console.ConsoleChannel;

public class Reply extends ChannelBasedConsoleCommand implements ConsoleCommand {
    public void execute(List<String> args) {
        super.execute(args);
    }

    protected void command(List<String> args) {
        final ServerTextChannel channel = ConsoleChannel.getCurrentChannel();

        if (args.size() < 2) {
            System.out.println("Not enough arguments were provided; not doing anything.");
            return;
        }

        final String messageId = args.remove(0);
        final Message message;

        try {
            message = channel.getMessageById(messageId).get();
        }
        catch (Exception e) {
            System.err.println("Couldn't find a message with ID " + messageId);
            return;
        }

        message.reply(String.join(" ", args));
    }
}
