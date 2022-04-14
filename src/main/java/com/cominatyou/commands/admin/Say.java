package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

public class Say {
    public static void run(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        if (messageArgs.size() == 0) { // Send a typing event if no message is specified
            message.deleteMessage();
            message.getChannel().type();
            return;
        }

        message.getMessage().getReferencedMessage().ifPresentOrElse(replyMessage -> { // Reply to the replied-to message if the invoking message was in reply to another.
            message.deleteMessage();
            replyMessage.reply(String.join(" ", messageArgs));
        }, () -> {
            message.deleteMessage();
            message.getChannel().sendMessage(String.join(" ", messageArgs));
        });
    }
}
