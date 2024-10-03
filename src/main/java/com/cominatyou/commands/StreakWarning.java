package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.TextCommand;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.MessageUtil;
import com.cominatyou.util.Values;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class StreakWarning extends TextCommand {
    private static final String warningsDisabledMessage = "Sure thing. Streak warnings have been disabled for you. Should you want to turn them back on, you can re-run this command.";
    private static final String warningsEnabledMessage = "Alright! Level warnings have been enabled again. If you want to turn them off again, re-run this command.";

    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (message.getChannel().getId() != Values.TESTING_CHANNEL && message.getChannel().getId() != Values.BOT_CHANNEL && message.getChannel().getType() != ChannelType.PRIVATE_CHANNEL) return;

        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean userPreference = user.getBoolean("streakwarningsdisabled");

        user.set("streakwarningsdisabled", userPreference ? "false" : "true");

        if (message.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) {
            message.getChannel().sendMessage(userPreference ? warningsEnabledMessage : warningsDisabledMessage);
        }
        else {
            MessageUtil.sendTextReply(message.getMessage(), userPreference ? warningsEnabledMessage : warningsDisabledMessage);
        }
    }
}
