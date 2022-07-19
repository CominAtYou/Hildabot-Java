package com.cominatyou.commands.admin;

import java.util.LinkedList;
import java.util.List;

import com.cominatyou.commands.Command;
import com.cominatyou.util.activities.ActivityStatus;
import com.cominatyou.util.versioning.Version;

import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.event.message.MessageCreateEvent;

/*
    I stole this code from my older bot, Niko. I do not know why half of the things in here are needed,
    (for example, the sublist that is immediately cleared) but they for some reason are.
    It works, though, so I'm not gonna question it.
*/
public class SetActivity implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;

        if (messageArgs.get(0).equalsIgnoreCase("reset")) {
            ActivityStatus.setActive(false);
            message.getApi().updateActivity(ActivityType.PLAYING, String.format("Version %s (%s)", Version.VERSION, Version.BUILD_NUMBER));
            message.getChannel().sendMessage("Activity has been reset!");
            return;
        }

        messageArgs = new LinkedList<String>(messageArgs);

        ActivityType type = null;
        final String proposedType = messageArgs.get(messageArgs.indexOf("--type") + 1);
        try {
            type = ActivityType.valueOf(proposedType.toUpperCase());
        }
        catch (Exception e) {
            message.getChannel().sendMessage(String.format("\"%s\" is not a valid activity type!", proposedType));
            return;
        }

        messageArgs.subList(messageArgs.indexOf("--type"), messageArgs.indexOf("--type") + 2).clear();

        ActivityStatus.setActive(true);
        final String activity = String.join(" ", messageArgs);
        message.getApi().updateActivity(type, activity);
        message.getMessage().reply(String.format("Activity set to \"%s %s\"!", proposedType.substring(0, 1).toUpperCase() + proposedType.substring(1), activity));
    }

    public String getName() {
        return "SetActivity";
    }
}
