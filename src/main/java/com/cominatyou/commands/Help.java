package com.cominatyou.commands;

import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

import com.cominatyou.helparticles.*;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Help implements Command {
    final static Map<String, EmbedBuilder> helpEmbeds = Map.ofEntries(
        entry("birthday", BirthdayHelp.ARTICLE_EMBED),
        entry("levelalert", LevelAlertHelp.ARTICLE_EMBED),
        entry("levelcheck", LevelCheckHelp.ARTICLE_EMBED),
        entry("stats", StatsHelp.ARTICLE_EMBED),
        entry("streakwarning", StreakWarningHelp.ARTICLE_EMBED),
        entry("submit", SubmitHelp.ARTICLE_EMBED)
    );

    public void execute(MessageCreateEvent message, List<String> args) {
        if (args.size() == 0) {
            message.getChannel().sendMessage(AvailableHelpArticles.HELP_ARTICLES_EMBED);
            return;
        }

        if (!helpEmbeds.containsKey(args.get(0).toLowerCase())) {
            message.getMessage().reply("There isn't a help article with that name!");
            return;
        }

        message.getChannel().sendMessage(helpEmbeds.get(args.get(0).toLowerCase()));
    }

    public String getName() {
        return "Help";
    }
}
