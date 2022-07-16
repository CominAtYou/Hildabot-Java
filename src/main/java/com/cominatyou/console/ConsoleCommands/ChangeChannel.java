package com.cominatyou.console.ConsoleCommands;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;

import com.cominatyou.App;
import com.cominatyou.console.ConsoleChannel;

public class ChangeChannel implements ConsoleCommand {
    public void execute(List<String> args) {
        if (args.size() == 0) {
            System.err.println("No channel ID was provided; not doing anything.");
            return;
        }

        final String channelId = args.get(0);
        final Optional<ServerTextChannel> possibleChannel = App.getClient().getServerTextChannelById(channelId);

        if (possibleChannel.isEmpty()) {
            System.err.println("Couldn't find a channel with ID " + channelId);
            return;
        }

        final ServerTextChannel channel = possibleChannel.get();
        ConsoleChannel.setChannel(channel);
        System.out.printf("Successfully changed channel to #%s in %s\n", channel.getName(), channel.getServer().getName());
    }
}
