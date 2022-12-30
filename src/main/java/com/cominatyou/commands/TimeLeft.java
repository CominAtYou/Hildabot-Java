package com.cominatyou.commands;

import java.time.ZonedDateTime;
import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.TextCommand;
import com.cominatyou.util.Values;

public class TimeLeft implements TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (message.getChannel().getId() != Values.BOT_CHANNEL && message.getChannel().getId() != Values.TESTING_CHANNEL) return;

        final long timeLeft = ZonedDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO).toLocalDate().atStartOfDay(Values.TIMEZONE_AMERICA_CHICAGO).plusDays(1).toEpochSecond();
        message.getChannel().sendMessage("The current submission period closes <t:" + timeLeft + ":R>, and the next one will start at <t:" + timeLeft + ":t>.");
    }

    public String getName() {
        return "TimeLeft";
    }
}
