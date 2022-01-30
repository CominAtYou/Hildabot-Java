package com.cominatyou.commands;

import java.util.List;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;

public class DisableXPGainForChannel {
    public static void add(MessageCreateEvent message, List<String> messageArgs) {
        final Role adminRole = message.getServer().get().getRoleById(492576994278637568L).get();
        if (!message.getMessageAuthor().asUser().get().getRoles(message.getServer().get()).contains(adminRole)) return;
    }
}
