package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;

public class CreationDate extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        if (messageArgs.size() < 1 && !messageArgs.get(0).matches("[0-9]{17,}")) return;

        final long id = Long.parseLong(messageArgs.get(0));

        message.getChannel().sendMessage("<t:" + ((id >> 22) + 1420070400000L) / 1000 + ">");
    }
}
