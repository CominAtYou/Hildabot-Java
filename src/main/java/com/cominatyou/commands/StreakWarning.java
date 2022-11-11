package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.Command;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class StreakWarning implements Command {
    private static final String warningsDisabledMessage = "Sure thing. Streak warnings have been disabled for you. Should you want to turn them back on, you can re-run this command.";
    private static final String warningsEnabledMessage = "Alright! Level warnings have been enabled again. If you want to turn them off again, re-run this command!";

    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (message.getChannel().getId() != Values.TESTING_CHANNEL && message.getChannel().getId() != Values.BOT_CHANNEL && message.getChannel().getType() != ChannelType.PRIVATE_CHANNEL) return;

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean userPreference = user.getBoolean("streakwarningsdisabled");

        if (!userPreference) {
            user.set("streakwarningsdisabled", "true");
            if (message.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) message.getChannel().sendMessage(warningsDisabledMessage);
            else message.getMessage().reply(warningsDisabledMessage);
        }
        else {
            user.set("streakwarningsdisabled", "false");
            if (message.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) message.getChannel().sendMessage(warningsEnabledMessage);
            else message.getMessage().reply(warningsEnabledMessage);
        }
    }

    public String getName() {
        return "StreakWarning";
    }
}
