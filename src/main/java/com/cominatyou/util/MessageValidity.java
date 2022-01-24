package com.cominatyou.util;

import org.javacord.api.entity.message.MessageType;
import org.javacord.api.event.message.MessageCreateEvent;

public class MessageValidity {
    public static boolean test(MessageCreateEvent message) {
        return message.getMessage().getType() != MessageType.NORMAL_WEBHOOK && !message.getMessageAuthor().isBotUser();
    }
}
