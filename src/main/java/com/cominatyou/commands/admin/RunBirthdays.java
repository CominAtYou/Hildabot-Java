package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;
import com.cominatyou.routinetasks.CheckForBirthdays;

public class RunBirthdays extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        try {
            message.getChannel().sendMessage("Running birthday task.");
            new CheckForBirthdays().execute(null);
        }
        catch (Exception e) {
            message.getChannel().sendMessage("Failed to run birthday task.");
            e.printStackTrace();
        }
    }
}
