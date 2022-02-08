package com.cominatyou;

import java.util.ArrayList;
import java.util.Arrays;

import com.cominatyou.commands.*;
import com.cominatyou.commands.admin.*;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class TextCommandHandler {
    public static void getCommand(MessageCreateEvent event) {
        ArrayList<String> messageArgs = new ArrayList<>(Arrays.asList(event.getMessage().getContent().substring(Config.PREFIX.length()).split(" +")));
        final String command = messageArgs.remove(0).toLowerCase();
        if (event.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) {
            switch (command) {
                case "levelalert": {
                    LevelAlert.setPreference(event);
                    break;
                }
                case "querydb": {
                    QueryDatabase.sendQuery(event, messageArgs);
                    break;
                }
            }
            return;
        }
        switch (command) {
            case "birthday": {
                Birthdays.run(event, messageArgs);
                break;
            }
            case "stats": {
                Stats.execute(event);
                break;
            }
            case "submit": {
                Submit.acceptSubmission(event, messageArgs);
                break;
            }
            case "restore": {
                Restore.restoreRank(event);
                break;
            }
            case "help": {
                Help.getArticle(event, messageArgs);
                break;
            }
            case "commit": {
                Commit.saveDB(event);
                break;
            }
            case "querydb": {
                QueryDatabase.sendQuery(event, messageArgs);
                break;
            }
            case "levelalert": {
                LevelAlert.setPreference(event);
                break;
            }
            case "say": {
                Say.run(event, messageArgs);
                break;
            }
        }
    }
}
