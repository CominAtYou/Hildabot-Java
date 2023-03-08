package com.cominatyou.store.items;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.SuccessfulPurchaseEmbed;
import com.cominatyou.xp.XPSystem;

public class MediumXPIncrease extends StoreItem {
    public MediumXPIncrease(String name, String description, String additionalInfo, String id, int price, PurchaseLimit purchaseLimit) throws IllegalArgumentException {
        super(name, description, additionalInfo, id, price, purchaseLimit);
    }

    public void giveItem(ButtonInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());

        final int currentXP = user.getXP();
        user.decrementKey("tokens", getPrice());
        user.incrementKey("xp", 100);

        final EmbedBuilder embed = SuccessfulPurchaseEmbed.create(interaction.getUser(), "You've recieved an extra 100 XP.");

        interaction.getMessage().createUpdater()
            .removeAllComponents()
            .removeAllEmbeds()
            .setContent("")
            .addEmbed(embed)
            .applyChanges();

        interaction.acknowledge();

        XPSystem.checkForLevelUp(currentXP, interaction.getUser(), interaction.getServer().get());
    }
}
