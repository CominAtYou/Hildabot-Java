package com.cominatyou.commands.admin;

import java.util.List;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;

public class SendTyping extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        message.deleteMessage();
        if (messageArgs.isEmpty()) {
            message.getChannel().type();
            return;
        }

        final String channelId = messageArgs.get(0);

        message.getApi().getChannelById(channelId).ifPresent(channel -> {
            if (channel.getType() != ChannelType.SERVER_TEXT_CHANNEL) return;
            channel.asServerTextChannel().get().type();
        });
    }
}
