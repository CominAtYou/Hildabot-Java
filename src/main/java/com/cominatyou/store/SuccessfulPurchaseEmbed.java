package com.cominatyou.store;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;

public class SuccessfulPurchaseEmbed {
    public static EmbedBuilder create(User user, String description) {
        final RedisUserEntry userEntry = new RedisUserEntry(user);
        return new EmbedBuilder()
            .setTitle("Purchase Successful!")
            .setDescription(description)
            .setAuthor(user)
            .setColor(Values.SUCCESS_GREEN)
            .addField("Token Balance", String.valueOf(userEntry.getInt("tokens")));
    }
}
