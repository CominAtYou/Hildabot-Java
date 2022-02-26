package com.cominatyou.commands.admin;

import java.util.List;

import com.cominatyou.util.Values;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Say {
    public static void run(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        final String messageString = String.join(" ", messageArgs);
        final EmbedBuilder embed = new EmbedBuilder().setColor(new java.awt.Color(Values.HILDA_BLUE));
        final MessageBuilder messageBuilder = new MessageBuilder();

        if (messageString.contains("_t:") && messageString.contains("_b:")) {
            embed.setTitle(messageString.substring(messageString.indexOf("_t:") + 3, messageString.indexOf("_b:")));
            embed.setDescription(messageString.substring(messageString.indexOf("_b:") + 3));
            messageBuilder.setContent(messageString.substring(0, messageString.indexOf("_t:")));
            messageBuilder.setEmbed(embed);
        }
        else if (messageString.contains("_t:")) {
            embed.setTitle(messageString.substring(messageString.indexOf("_t:") + 3));
            messageBuilder.setContent(messageString.substring(0, messageString.indexOf("_t:")));
            messageBuilder.setEmbed(embed);
        }
        else if (messageString.contains("_b:")) {
            embed.setTitle(messageString.substring(messageString.indexOf("_b:") + 3));
            messageBuilder.setContent(messageString.substring(0, messageString.indexOf("_:")));
            messageBuilder.setEmbed(embed);
        }
        else {
            messageBuilder.setContent(messageString);
        }

        message.getMessage().getReferencedMessage().ifPresentOrElse(replyMessage -> { // Reply to the replied-to message if the invoking message was in reply to another.
            message.deleteMessage();
            messageBuilder.replyTo(replyMessage);
        }, () -> {
            message.deleteMessage();
            messageBuilder.send(message.getChannel());
        });
    }
}
