package com.cominatyou.commands;

import com.cominatyou.db.RedisInstance;

import org.javacord.api.event.message.MessageCreateEvent;

public class GetXP {
    public static void execute(MessageCreateEvent message) {
        final String redisKey = "users:" + message.getMessageAuthor().getIdAsString() + ":xp";
        final String currentXP = RedisInstance.getInstance().get(redisKey) == null ? "0" : RedisInstance.getInstance().get(redisKey);
        message.getChannel().sendMessage(currentXP);
    }
}
