package com.cominatyou.commands.admin;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.util.Values;

import org.javacord.api.event.message.MessageCreateEvent;

public class Commit {
    public static void saveDB(MessageCreateEvent message) {
        if (message.getMessageAuthor().getId() != Values.COMIN_USER_ID) return;

        final String response = RedisInstance.getInstance().save();

        if (response.equalsIgnoreCase("ok")) {
            message.getChannel().sendMessage("Database has been written to disk!");
        }
        else {
            message.getChannel().sendMessage("Something went wrong. Info is available in the log.");
            System.err.println("[REDIS] Save failed: " + response);
        }
    }
}
