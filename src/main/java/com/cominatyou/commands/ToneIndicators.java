package com.cominatyou.commands;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.cominatyou.util.Values;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class ToneIndicators {
    private static final Pattern regex = Pattern.compile("/(hj|gen|srs|nsrs|pos|pc|neu|neg|nc|ly|lh|nm|lu|nbh|rh|rt|ij|m|li|hyp|f|th|cb|p|r|c|l|j|s|g|t|s)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    private static final Map<String, String> toneIndicators = Map.ofEntries(
        Map.entry("j", "Joking"),
        Map.entry("hj", "Half-joking"),
        Map.entry("s", "Sarcastic"),
        Map.entry("gen", "Genuine"),
        Map.entry("g", "Genuine"),
        Map.entry("srs", "Serious"),
        Map.entry("nsrs", "Not serious"),
        Map.entry("pos", "Positive connotation"),
        Map.entry("pc", "Positive connotation"),
        Map.entry("neu", "Neutral connotation"),
        Map.entry("neg", "Negative connotation"),
        Map.entry("nc", "Negative connotation"),
        Map.entry("p", "Platonic"),
        Map.entry("r", "Romantic"),
        Map.entry("c", "Copypasta"),
        Map.entry("l", "Lyrics"),
        Map.entry("ly", "Lyrics"),
        Map.entry("lh", "Light-hearted"),
        Map.entry("nm", "Not mad"),
        Map.entry("lu", "A little upset"),
        Map.entry("nbh", "Used to signify that the message is directed towards **nobody here**"),
        Map.entry("rh", "Rhetorical question"),
        Map.entry("rt", "Rhetorical question"),
        Map.entry("t", "Teasing"),
        Map.entry("ij", "Inside joke"),
        Map.entry("m", "Metaphorically"),
        Map.entry("li", "Literally"),
        Map.entry("hyp", "Hyperbole"),
        Map.entry("f", "Fake"),
        Map.entry("th", "Threat"),
        Map.entry("cb", "Clickbait")
    );

    public static void define(MessageCreateEvent message, List<String> messageArgs) {
        message.getMessage().getReferencedMessage().ifPresentOrElse(reply -> {
            final String[] messageToneIndicators = regex.matcher(reply.getContent()).results().map(MatchResult::group).toArray(String[]::new);

            if (messageToneIndicators.length == 0) {
                message.getMessage().reply("That message doesn't have any tone indicators!");
                return;
            }

            final EmbedBuilder embed = new EmbedBuilder().setTitle("Tone Indicators").setColor(new java.awt.Color(Values.HILDA_BLUE)).setAuthor(reply.getAuthor());
            for (int i = 0; i < Math.min(messageToneIndicators.length, 5); i++) {
                final String toneIndicator = messageToneIndicators[i].toLowerCase();
                embed.addField(toneIndicator, toneIndicators.get(toneIndicator.substring(1)));
            }

            message.getChannel().sendMessage(embed);
        }, () -> {
            message.getMessage().reply("You need to reply to a message when using the define command!");
        });
    }
}
