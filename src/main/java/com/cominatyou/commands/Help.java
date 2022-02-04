package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.helparticles.BirthdaysHelp;
import com.cominatyou.helparticles.SubmitHelp;

import org.javacord.api.event.message.MessageCreateEvent;

public class Help {
    public static void getArticle(MessageCreateEvent message, List<String> messageArgs) {
        if (messageArgs.size() == 0) {
            message.getMessage().reply("Please specify the help article you want. You can do either `h!help submit` or `h!help birthdays`!");
            return;
        }
        switch (messageArgs.get(0)) {
            case "birthdays": {
                BirthdaysHelp.sendHelpArticle(message);
                break;
            }
            case "submit": {
                SubmitHelp.sendHelpArticle(message);
                break;
            }
        }
    }
}
