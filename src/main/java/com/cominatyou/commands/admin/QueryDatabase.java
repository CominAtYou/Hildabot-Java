package com.cominatyou.commands.admin;

import java.util.List;

import com.cominatyou.commands.Command;
import com.cominatyou.db.RedisInstance;

import org.javacord.api.event.message.MessageCreateEvent;

public class QueryDatabase implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        if (messageArgs.size() == 0) {
            message.getChannel().sendMessage("A key was not supplied!");
            return;
        }

        if (messageArgs.size() == 1) {
            final String key = String.join(" ", messageArgs);
            final String response = RedisInstance.getInstance().get(key);
            message.getChannel().sendMessage(response == null ? "Uhhh... I got nothing." : response);
        }
        else if (messageArgs.size() > 1) {
            final String command = messageArgs.remove(0);
            final String key = String.join(" ", messageArgs);
            if (command.equalsIgnoreCase("lrange")) {
                final List<String> response = RedisInstance.getInstance().lrange(key, 0, -1);
                final String resp = String.join(", ", response);
                message.getChannel().sendMessage(response.size() == 0 ? "Nothing here but Niko and the sun!" : resp); // Yes, I will continue to put game references in my software
            }
        }
    }
}
