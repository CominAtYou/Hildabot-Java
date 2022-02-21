package com.cominatyou.eventhandlers;

import com.cominatyou.Config;
import com.cominatyou.TextCommandHandler;
import com.cominatyou.util.MessageValidity;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class MessageCreate {
    public static void route(MessageCreateEvent event) {
        if (!MessageValidity.test(event)) return;
        if (!event.getMessageContent().startsWith(Config.PREFIX) && event.getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL) {
            XPSystem.giveXPForMessage(event);
        }
        else {
            TextCommandHandler.getCommand(event);
        }
    }
}
