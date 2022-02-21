package com.cominatyou.commands.admin;

import com.cominatyou.util.versioning.Version;

import org.javacord.api.event.message.MessageCreateEvent;

public class GetVersion {
    public static void sendVersion(MessageCreateEvent message) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        message.getChannel().sendMessage(String.format("Version %s / Build %s", Version.VERSION, Version.BUILD_NUMBER));
    }
}
