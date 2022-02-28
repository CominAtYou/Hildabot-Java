package com.cominatyou.eventhandlers.kudos;

import com.cominatyou.db.RedisUserEntry;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageType;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;

public class Kudos {
    public static void tally(ReactionAddEvent reaction) {
        if (reaction.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) return;
        if (reaction.getEmoji() != reaction.getServer().get().getCustomEmojiById(539313415425097728L).get()) return;


        final Message message;

        try {
            message = reaction.requestMessage().get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final RedisUserEntry giver = new RedisUserEntry(reaction.getUserId()); // Person who reacted to the message
        giver.incrementKey("kudos:given");

        if (message.getType() == MessageType.NORMAL) {
            final RedisUserEntry receiver = new RedisUserEntry(message.getAuthor());
            receiver.incrementKey("kudos:received");
        }

    }

    public static void remove(ReactionRemoveEvent event) {
        if (event.getChannel().getType() == ChannelType.PRIVATE_CHANNEL) return;
        if (event.getEmoji() != event.getServer().get().getCustomEmojiById(539313415425097728L).get()) return;

        final RedisUserEntry giver = new RedisUserEntry(event.getUserId());
        giver.decrementKey("kudos:given");

        final Message message;

        try {
            message = event.requestMessage().get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (message.getType() == MessageType.NORMAL) {
            final RedisUserEntry reciever = new RedisUserEntry(event.getMessageAuthor().get());
            reciever.decrementKey("kudos:received");
        }
    }
}
