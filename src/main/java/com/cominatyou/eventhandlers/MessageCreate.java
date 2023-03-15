package com.cominatyou.eventhandlers;

import com.cominatyou.Config;
import com.cominatyou.TextCommandHandler;
import com.cominatyou.util.MessageValidity;
import com.cominatyou.util.Values;
import com.cominatyou.xp.XPSystem;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.MessageType;
import org.javacord.api.event.message.MessageCreateEvent;

public class MessageCreate {
    public static void route(MessageCreateEvent event) {
        if (!System.getProperty("os.name").equalsIgnoreCase("Linux") && !event.getMessageAuthor().isBotOwner()) return;

        if (!MessageValidity.test(event)) return;
        if (!event.getMessageContent().startsWith(Config.PREFIX) && event.getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL && event.getServer().get().getId() == Values.BASE_GUILD_ID && (event.getMessage().getType() == MessageType.NORMAL || event.getMessage().getType() == MessageType.REPLY)) {
            XPSystem.giveXPForMessage(event);
        }
        else {
            TextCommandHandler.getCommand(event);
        }
    }
}
