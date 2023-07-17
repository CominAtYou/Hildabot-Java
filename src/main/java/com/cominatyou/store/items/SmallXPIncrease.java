package com.cominatyou.store.items;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.SuccessfulPurchaseEmbed;
import com.cominatyou.xp.XPSystem;

public class SmallXPIncrease extends StoreItem {
    public SmallXPIncrease() {
        super("50 XP", "Instantly get an extra 50 XP!", null, "smallxp", 150, PurchaseLimit.UNLIMITED);
    }

    public void giveItem(ButtonInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());

        final int currentXP = user.getXP();
        user.decrementKey("tokens", getPrice());
        user.incrementKey("xp", 50);

        final EmbedBuilder embed = SuccessfulPurchaseEmbed.create(interaction.getUser(), "You've received an extra 50 XP.");

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
