package com.cominatyou.util;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.mention.AllowedMentionsBuilder;

public class MessageUtil {
    /**
     * Sends a text-only reply to a message without mentioning the user who sent the message.
     * @param message The message to reply to.
     * @param text The text to send.
     */
    public static void sendTextReply(Message message, String text) {
        new MessageBuilder()
            .replyTo(message)
            .setContent(text)
            .setAllowedMentions(new AllowedMentionsBuilder().setMentionRepliedUser(false).build())
            .send(message.getChannel());
    }

    /**
     * Sends a text-only reply to a message.
     * @param message The message to reply to.
     * @param text The text to send.
     * @param mention Whether or not to mention the user who sent the message.
     */
    public static void sendTextReply(Message message, String text, boolean mention) {
        new MessageBuilder()
            .replyTo(message)
            .setContent(text)
            .setAllowedMentions(new AllowedMentionsBuilder().setMentionRepliedUser(mention).build())
            .send(message.getChannel());
    }
}
