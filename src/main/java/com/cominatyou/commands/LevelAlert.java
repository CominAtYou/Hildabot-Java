package com.cominatyou.commands;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.util.Values;

import org.javacord.api.event.message.MessageCreateEvent;

public class LevelAlert {
    public static void setPreference(MessageCreateEvent message) {
        if (message.getChannel().getId() != Values.BOT_CHANNEL) return;
        final String userId = message.getMessageAuthor().getIdAsString();
        final boolean userPreference = RedisInstance.getBoolean("users:" + userId + ":levelalertsdisabled");

        if (userPreference) {
            RedisInstance.getInstance().set("users:" + userId + ":levelalertsdisabled", "true");
            message.getMessage().reply("Alright! Level alerts have been disabled for you. If you want to turn them back on in the future, run this command again.");
        }
        else {
            RedisInstance.getInstance().set("users:" + userId + ":levelalertsdisabled", "false");
            message.getMessage().reply("Alright! Level alerts have been enabled for you. If you want to turn them off in the future, run this command again.");
        }
    }
}
