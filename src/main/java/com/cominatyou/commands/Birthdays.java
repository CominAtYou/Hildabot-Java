package com.cominatyou.commands;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.commands.birthdays.*;
import com.cominatyou.commands.interfaces.Command;

public class Birthdays implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (messageArgs.size() == 0 || messageArgs.size() == 1 && !messageArgs.get(0).equalsIgnoreCase("list")) {
            message.getMessage().reply("Looks like you're missing some arguments. Please make sure you provided a command (set|edit) and a date (i.e. `h!birthday set 06-21`), or list and a month if you want to view upcoming birthdays!");
        }
        else if (messageArgs.get(0).equalsIgnoreCase("set")) {
            SetBirthday.set(message, messageArgs);
        } else if (messageArgs.get(0).equalsIgnoreCase("edit")) {
            EditBirthday.edit(message, messageArgs);
        }
        else if (messageArgs.get(0).equalsIgnoreCase("list")) {
            ListBirthdays.list(message, messageArgs);
        }
    }

    public String getName() {
        return "Birthdays";
    }
}
