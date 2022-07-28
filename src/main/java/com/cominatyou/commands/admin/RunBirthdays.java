package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.Command;
import com.cominatyou.routinetasks.CheckForBirthdays;

public class RunBirthdays implements Command {
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

    public String getName() {
        return "RunBirthdays";
    }
}
