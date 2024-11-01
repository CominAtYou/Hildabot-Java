package com.cominatyou.commands;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;

public class Birthdays extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (messageArgs.isEmpty()) {
            message.getChannel().sendMessage("These commands have been deprecated. Please use the `/birthday` command instead.");
            return;
        }

        final String arg = messageArgs.get(0);
        if (arg.equalsIgnoreCase("set") || arg.equalsIgnoreCase("edit") || arg.equalsIgnoreCase("list")) {
            message.getChannel().sendMessage("This command has been deprecated. Please use </birthday " + arg + ":1011153003853643816> instead.");
            return;
        }
        else {
            message.getChannel().sendMessage("These commands have been deprecated. Please use the `/birthday` command instead.");
            return;
        }
    }
}
