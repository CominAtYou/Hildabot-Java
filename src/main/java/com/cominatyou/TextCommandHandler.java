package com.cominatyou;

import java.util.ArrayList;
import java.util.Arrays;

import com.cominatyou.commands.Birthdays;
import com.cominatyou.commands.Help;
import com.cominatyou.commands.LevelAlert;
import com.cominatyou.commands.Restore;
import com.cominatyou.commands.Stats;
import com.cominatyou.commands.Submit;
import com.cominatyou.commands.admin.Commit;
import com.cominatyou.commands.admin.QueryDatabase;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.message.MessageCreateEvent;

public class TextCommandHandler {
    public static void getCommand(MessageCreateEvent event) {
        ArrayList<String> messageArgs = new ArrayList<String>(Arrays.asList(event.getMessage().getContent().substring(Config.PREFIX.length()).split(" +")));
        final String command = messageArgs.remove(0).toLowerCase();
        if (event.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) {
            if (command.equals("levelalert")) {
                LevelAlert.setPreference(event);
            }
            return;
        }
        switch (command) {
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
        }
    }
}
