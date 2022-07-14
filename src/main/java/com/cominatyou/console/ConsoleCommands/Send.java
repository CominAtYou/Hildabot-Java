package com.cominatyou.console.ConsoleCommands;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;

import com.cominatyou.App;

public class Send implements ConsoleCommand {
    public void execute(List<String> args) {
        final String channelId = args.remove(0);

        final Optional<ServerTextChannel> possibleChannel = App.getClient().getServerTextChannelById(channelId);
        if (possibleChannel.isEmpty()) {
            System.out.println("Couldn't find a channel with ID " + channelId);
            return;
        }

        final ServerTextChannel channel = possibleChannel.get();

        channel.sendMessage(String.join(", ", args));
    }

}
