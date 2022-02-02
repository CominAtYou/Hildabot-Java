package com.cominatyou;

import java.util.concurrent.ThreadLocalRandom;

import com.cominatyou.util.Values;
import com.cominatyou.xp.RankUtil;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

public class Welcome {
    private final static String[] welcomeMessages = {"Come on in and have a seat around the fire!", "We're glad to have you here!", "Glad you could make it!", "Make yourself at home!", "The bells ring to welcome your arrival."};

    public static void greet(ServerMemberJoinEvent event) {
        if (event.getUser().isBot()) return;

        final Role timeWorm = event.getServer().getRoleById(RankUtil.getRankFromLevel(1).getId()).get();
        event.getUser().addRole(timeWorm);

        final int index = ThreadLocalRandom.current().nextInt(0, welcomeMessages.length);
        final String greeting = welcomeMessages[index];
        final int memberCount = event.getServer().getMemberCount();

        final String welcomeMessage = String.format("**#%d - Welcome to Hildacord,** <@%d>**!** %s\n\nHildacord is a great place for everyone from all around the world to come around a show that they all love: Hilda!", memberCount, event.getUser().getId(), greeting);

        EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(event.getUser().getDiscriminatedName(), null, event.getUser().getAvatar())
            .addField("New to Discord?", "Don't worry! The folks at Discord HQ have got an [article](https://support.discord.com/hc/en-us/articles/360045138571-Beginner-s-Guide-to-Discord) to help you get up to speed!")
            .addInlineField("To get started, please read:", "- [Rules](https://discord.com/channels/492572315138392064/657825719522689045)\n- [Server Info](https://discord.com/channels/492572315138392064/657718082831384606)\n- [Plural Info](https://discord.com/channels/492572315138392064/787521898170810368)")
            .addInlineField("Channels to check out!", "- [Show Discussion](https://discord.com/channels/492572315138392064/492573040027369483)\n- [Movie Discussion](https://discord.com/channels/492572315138392064/498574984294301696)\n- [Fanart](https://discord.com/channels/492572315138392064/492580873628286976)")
            .setImage("https://media.giphy.com/media/ygBBMqVPvd4zvCRT1y/giphy.gif")
            .setColor(new java.awt.Color(Values.HILDA_BLUE))
            .setFooter("Have any questions? Ask around or DM a staff member for help!", event.getServer().getIcon().get());
        event.getServer().getTextChannelById(830495332474552360L).get().sendMessage(welcomeMessage, embed);
    }
}
