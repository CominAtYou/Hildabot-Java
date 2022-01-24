package com.cominatyou;

import java.util.ArrayList;
import java.util.Arrays;

import com.cominatyou.commands.Birthdays;
import com.cominatyou.commands.GetXP;
import com.cominatyou.commands.Stats;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class TextCommandHandler {
    public static void getCommand(MessageCreateEvent event) {
        ArrayList<String> messageArgs = new ArrayList<String>(Arrays.asList(event.getMessage().getContent().substring(Config.getPrefix().length()).split(" +")));
        final String command = messageArgs.remove(0).toLowerCase();
        if (event.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) {
            if (command.equals("levelAlert")) {
                // do stuff
            }
            return;
        }
        switch (command) {
            case "getxp": {
                GetXP.execute(event);
                break;
            }
            case "birthday": {
                if (messageArgs.get(0).equalsIgnoreCase("set")) {
                    Birthdays.set(event, messageArgs);
                } else if (messageArgs.get(0).equalsIgnoreCase("edit")) {
                    Birthdays.edit(event, messageArgs);
                }
                break;
            }
            case "stats": {
                Stats.execute(event);
            }
        }
    }
}
