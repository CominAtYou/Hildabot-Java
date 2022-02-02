package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

public class Say {
    public static void run(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        message.getMessage().getReferencedMessage().ifPresentOrElse(replyMessage -> {
            message.deleteMessage();
            replyMessage.reply(String.join(" ", messageArgs));
        }, () -> {
            final String sayMessage = String.join(" ", messageArgs);
            message.deleteMessage();
            message.getChannel().sendMessage(sayMessage);
        });
    }
}
