package com.cominatyou.commands;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.util.Values;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class LevelAlert {
    private final static String alertsDisabledMessage = "Alright! Level alerts have been disabled for you. If you want to turn them back on in the future, run this command again.";
    private final static String alertsEnabledMessage = "Alright! Level alerts have been enabled for you. If you want to turn them off in the future, run this command again.";

    public static void setPreference(MessageCreateEvent message) {
        if (message.getChannel().getId() != Values.BOT_CHANNEL && message.getChannel().getType() != ChannelType.PRIVATE_CHANNEL) return;
        final String userId = message.getMessageAuthor().getIdAsString();
        final boolean userPreference = RedisInstance.getBoolean("users:" + userId + ":levelalertsdisabled");

        if (!userPreference) {
            RedisInstance.getInstance().set("users:" + userId + ":levelalertsdisabled", "true");
            if (message.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) message.getChannel().sendMessage(alertsDisabledMessage);
            else message.getMessage().reply(alertsDisabledMessage);
        }
        else {
            RedisInstance.getInstance().set("users:" + userId + ":levelalertsdisabled", "false");
            if (message.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) message.getChannel().sendMessage(alertsEnabledMessage);
            else message.getMessage().reply(alertsEnabledMessage);
        }
    }
}
