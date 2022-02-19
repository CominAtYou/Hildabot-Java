package com.cominatyou.commands;

import org.javacord.api.event.message.MessageCreateEvent;

public class Help {
    public static void sendHelp(MessageCreateEvent message) {
        message.getMessage().reply("The help articles for Hildabot are available here: <http://hildabot.cominatyou.com>");
    }
}
