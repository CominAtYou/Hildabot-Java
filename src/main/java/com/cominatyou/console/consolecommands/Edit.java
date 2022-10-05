package com.cominatyou.console.consolecommands;

import java.util.List;

import org.javacord.api.entity.message.Message;

import com.cominatyou.console.ConsoleChannel;

public class Edit extends ChannelBasedConsoleCommand implements ConsoleCommand {
    public void execute(List<String> args) {
        super.execute(args);
    }

    protected void command(List<String> args) {
        if (args.size() < 2) {
            System.out.println("Not enough arguments were provided; not doing anything.");
            return;
        }

        final String messageId = args.remove(0);
        final Message message;

        try {
            message = ConsoleChannel.getCurrentChannel().getMessageById(messageId).get();
        } catch (Exception e) {
            System.err.println("Couldn't find a message with ID" + args.remove(0));
            return;
        }

        if (!message.getAuthor().isYourself()) {
            System.err.println("That message cannot be edited, as it was not sent by the bot.");
            return;
        }

        message.edit(String.join(" ", args));
    }
}
