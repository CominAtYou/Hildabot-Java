package com.cominatyou.commands.admin;

import java.util.List;

import com.cominatyou.commands.Command;

import org.javacord.api.event.message.MessageCreateEvent;

public class Say implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        message.deleteMessage();
        if (messageArgs.size() == 0) return;

        message.getMessage().getReferencedMessage().ifPresentOrElse(replyMessage -> { // Reply to the replied-to message if the invoking message was in reply to another.
            replyMessage.reply(String.join(" ", messageArgs));
        }, () -> {
            message.getChannel().sendMessage(String.join(" ", messageArgs));
        });
    }
}
