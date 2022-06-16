package com.cominatyou.helparticles;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.cominatyou.App;
import com.cominatyou.util.Values;
import com.cominatyou.util.versioning.Version;

public class EconomyTestHelp {
    public static final EmbedBuilder ARTICLE_EMBED = new EmbedBuilder()
        .setTitle("Tokens Test")
        .addField("What is it?", "Tokens are something we're testing to help improve your submit experience a little bit. In the future, you'll be able to get things like a one-week extension for your streak, a little extra XP for your next submission, among other things.")
        .addField("How do I participate?", "Participation is chosen at random - you have a 1 to 25 percent chance of getting in, with a maximum of 30 or so testers.\n" + "We need to make sure everything is working right, so in order to ensure that, participation is limited to those who have a high enough streak and have attained a certain level.")
        .addField("How do I see my token balance?", "`h!stats` should help with that.")
        .addField("Will I be removed from the test if my streak expires?", "Nope! However, if you're inactive for a significant amount of time, your spot might be put up for grabs.")
        .setColor(Values.HILDA_BLUE)
        .setFooter("Hildabot " + Version.VERSION_STRING, App.getClient().getYourself().getAvatar());
}
