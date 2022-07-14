package com.cominatyou.console.ConsoleCommands;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;

import com.cominatyou.App;

public class Delete implements ConsoleCommand {
    public void execute(List<String> args) {
        final String channelId = args.remove(0);
        final Optional<ServerTextChannel> possibleChannel = App.getClient().getServerTextChannelById(channelId);

        if (possibleChannel.isEmpty()) {
            System.err.println("Couldn't find a channel with ID " + channelId);
            return;
        }

        final ServerTextChannel channel = possibleChannel.get();
        final String messageId = args.remove(0);

        final Message message;
        try {
            message = channel.getMessageById(messageId).get();
        }
        catch (Exception e) {
            System.err.println("Couldn't find a message with ID " + messageId);
            return;
        }

        if (!message.getAuthor().isYourself()) {
            System.out.println("That message was not sent by the bot, not deleting.");
            return;
        }

        message.delete();
    }
}
