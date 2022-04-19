package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.util.Command;

import org.javacord.api.event.message.MessageCreateEvent;

public class Help extends Command {
    public void execute(MessageCreateEvent message, List<String> args) {
        message.getMessage().reply("The help articles for Hildabot are available here: <https://hildabot.cominatyou.com/wiki/>");
    }
}
